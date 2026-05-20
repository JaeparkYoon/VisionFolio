package jpyoon.example.visionfolio.data.portfolio

import jpyoon.example.visionfolio.core.common.di.AppCoroutineScope
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotDao
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotEntity
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.categories
import jpyoon.example.visionfolio.domain.model.formatter.KrwFormatter
import jpyoon.example.visionfolio.domain.model.formatter.PercentFormatter
import jpyoon.example.visionfolio.domain.model.CategoryContribution
import jpyoon.example.visionfolio.domain.model.Period
import jpyoon.example.visionfolio.domain.model.PortfolioSeries
import jpyoon.example.visionfolio.domain.model.SeriesPoint
import jpyoon.example.visionfolio.domain.model.displayName
import jpyoon.example.visionfolio.core.repository.api.SeriesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

@AppSingleton
class SeriesRepositoryImpl @Inject constructor(
    private val dao: SnapshotDao,
    @AppCoroutineScope private val appScope: CoroutineScope,
) : SeriesRepository {

    private val zone: ZoneId = ZoneId.systemDefault()

    init {
        seedIfEmpty()
    }

    private fun seedIfEmpty() {
        appScope.launch(Dispatchers.IO) {
            SnapshotSeedData.generate(zone).forEach { snap ->
                if (dao.exists(snap.timestamp) == 0) {
                    dao.upsert(snap)
                }
            }
        }
    }

    override fun observeSeries(
        period: Period,
        customRange: ClosedRange<Long>?,
    ): Flow<PortfolioSeries> = rangeFlow(period, customRange).map { entities ->
        PortfolioSeries(
            period = period,
            points = entities.map { SeriesPoint(it.timestamp, it.totalKrw) },
        )
    }

    override fun observeContributions(period: Period): Flow<List<CategoryContribution>> =
        rangeFlow(period, null).map { snapshots ->
            buildContributions(snapshots)
        }

    override fun observeAiSummary(period: Period): Flow<String> =
        rangeFlow(period, null).map { snapshots -> buildAiSummary(period, snapshots) }

    override fun observeOldestTimestamp(): Flow<Long?> = dao.observeOldestTimestamp()

    override fun observeNewestTimestamp(): Flow<Long?> = dao.observeNewestTimestamp()

    override fun observeLatestBeforeToday(): Flow<SeriesPoint?> {
        val todayStart = Instant.now()
            .atZone(zone)
            .toLocalDate()
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()
        return dao.observeLatestBefore(todayStart).map { entity ->
            entity?.let { SeriesPoint(it.timestamp, it.totalKrw) }
        }
    }

    private fun rangeFlow(
        period: Period,
        customRange: ClosedRange<Long>?,
    ): Flow<List<SnapshotEntity>> {
        val (from, to) = rangeFor(period, customRange)
        return dao.observeRange(from, to)
    }

    private fun rangeFor(period: Period, customRange: ClosedRange<Long>?): Pair<Long, Long> {
        if (period == Period.CUSTOM && customRange != null) {
            return customRange.start to customRange.endInclusive
        }
        val now = System.currentTimeMillis()
        val days = when (period) {
            Period.D1 -> 1
            Period.W1 -> 7
            Period.M1 -> 30
            Period.M3 -> 90
            Period.M6 -> 180
            Period.Y1 -> 365
            Period.ALL -> Int.MAX_VALUE / 2
            Period.CUSTOM -> 30
        }
        val from = if (days >= Int.MAX_VALUE / 2) 0L else {
            Instant.ofEpochMilli(now)
                .atZone(zone)
                .minusDays(days.toLong())
                .toLocalDate()
                .atStartOfDay(zone)
                .toInstant()
                .toEpochMilli()
        }
        return from to now
    }

    private fun buildContributions(snapshots: List<SnapshotEntity>): List<CategoryContribution> {
        if (snapshots.size < 2) return emptyList()
        val first = snapshots.first().categories()
        val last = snapshots.last().categories()
        val keys = (first.keys + last.keys).toSet()
        return keys.map { cat ->
            val start = first[cat] ?: 0L
            val end = last[cat] ?: 0L
            val change = end - start
            val pct = if (start == 0L) 0.0 else change.toDouble() / start.toDouble() * 100.0
            CategoryContribution(category = cat, changePct = pct, changeKrw = change)
        }.sortedByDescending { it.changePct }
    }

    private fun buildAiSummary(period: Period, snapshots: List<SnapshotEntity>): String {
        if (snapshots.isEmpty()) return "기록된 스냅샷이 없습니다. 며칠 뒤 다시 확인해 주세요."
        if (snapshots.size < 2) {
            val only = snapshots.first()
            return "첫 스냅샷이 기록되었습니다. 현재 평가액은 ${KrwFormatter.format(only.totalKrw, compact = true)} 입니다."
        }
        val first = snapshots.first().totalKrw
        val last = snapshots.last().totalKrw
        val diff = last - first
        val pct = if (first == 0L) 0.0 else diff.toDouble() / first.toDouble() * 100.0
        val direction = if (diff >= 0) "상승" else "하락"
        val contributions = buildContributions(snapshots)
        val top = contributions.firstOrNull()
        val bottom = contributions.lastOrNull()
        val periodLabel = when (period) {
            Period.D1 -> "최근 1일"
            Period.W1 -> "최근 1주"
            Period.M1 -> "최근 1개월"
            Period.M3 -> "최근 3개월"
            Period.M6 -> "최근 6개월"
            Period.Y1 -> "최근 1년"
            Period.ALL -> "전체 기간"
            Period.CUSTOM -> "선택 기간"
        }
        return buildString {
            append("$periodLabel 동안 총 자산은 ")
            append(PercentFormatter.format(pct))
            append(" ($direction) 하였습니다.")
            if (top != null && top.changePct > 0) {
                append(" 가장 큰 기여는 ${top.category.displayName}(${PercentFormatter.format(top.changePct)}) 이었고")
            }
            if (bottom != null && bottom.changePct < 0) {
                append(", 반대로 ${bottom.category.displayName}(${PercentFormatter.format(bottom.changePct)}) 가 하방을 눌렀습니다.")
            } else {
                append(".")
            }
        }
    }
}
