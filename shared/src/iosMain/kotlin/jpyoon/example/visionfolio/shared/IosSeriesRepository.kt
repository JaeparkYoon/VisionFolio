package jpyoon.example.visionfolio.shared

import jpyoon.example.visionfolio.core.common.di.AppSingleton
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotDao
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotEntity
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.categories
import jpyoon.example.visionfolio.core.repository.api.SeriesRepository
import jpyoon.example.visionfolio.domain.model.CategoryContribution
import jpyoon.example.visionfolio.domain.model.Period
import jpyoon.example.visionfolio.domain.model.PortfolioSeries
import jpyoon.example.visionfolio.domain.model.SeriesPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject

/**
 * Room KMP SnapshotDao-based iOS SeriesRepository implementation.
 * Mirrors Android's SeriesRepositoryImpl without java.time or Context dependencies.
 */
@AppSingleton
@Inject
class IosSeriesRepository(
    private val dao: SnapshotDao,
) : SeriesRepository {

    private val zone = TimeZone.currentSystemDefault()

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
        rangeFlow(period, null).map { snapshots -> buildContributions(snapshots) }

    override fun observeAiSummary(period: Period): Flow<String> =
        rangeFlow(period, null).map { snapshots -> buildAiSummary(period, snapshots) }

    override fun observeOldestTimestamp(): Flow<Long?> = dao.observeOldestTimestamp()

    override fun observeNewestTimestamp(): Flow<Long?> = dao.observeNewestTimestamp()

    override fun observeLatestBeforeToday(): Flow<SeriesPoint?> {
        val nowInstant = Instant.fromEpochMilliseconds(Clock.System.now().toEpochMilliseconds())
        val todayStart = nowInstant.toLocalDateTime(zone).date.atStartOfDayIn(zone).toEpochMilliseconds()
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
        val now = Clock.System.now().toEpochMilliseconds()
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
            val nowInstant = Instant.fromEpochMilliseconds(now)
            val localDate = nowInstant.toLocalDateTime(zone).date
            val targetDate = localDate.minus(days, DateTimeUnit.DAY)
            targetDate.atStartOfDayIn(zone).toEpochMilliseconds()
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

    /** Simple percent formatter that avoids expect/actual dependencies. */
    private fun formatPct(v: Double): String {
        val sign = if (v > 0) "+" else if (v < 0) "-" else ""
        val abs = if (v < 0) -v else v
        val intPart = abs.toLong()
        val fracPart = ((abs - intPart) * 100 + 0.5).toLong()
        return "$sign$intPart.${fracPart.toString().padStart(2, '0')}%"
    }

    private fun buildAiSummary(period: Period, snapshots: List<SnapshotEntity>): String {
        if (snapshots.isEmpty()) return "기록된 스냅샷이 없습니다."
        if (snapshots.size < 2) {
            val only = snapshots.first()
            return "첫 스냅샷이 기록되었습니다. 총 자산: ${only.totalKrw}원"
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
            append("$periodLabel ${formatPct(pct)} $direction")
            if (top != null && top.changePct > 0) {
                append(". 최고 기여: ${top.category.name} (${formatPct(top.changePct)})")
            }
            if (bottom != null && bottom.changePct < 0) {
                append(". 최저: ${bottom.category.name} (${formatPct(bottom.changePct)})")
            } else {
                append(".")
            }
        }
    }
}
