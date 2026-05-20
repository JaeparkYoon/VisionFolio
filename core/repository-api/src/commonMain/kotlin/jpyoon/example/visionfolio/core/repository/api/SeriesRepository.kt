package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.CategoryContribution
import jpyoon.example.visionfolio.domain.model.Period
import jpyoon.example.visionfolio.domain.model.PortfolioSeries
import jpyoon.example.visionfolio.domain.model.SeriesPoint
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {
    fun observeSeries(period: Period, customRange: ClosedRange<Long>? = null): Flow<PortfolioSeries>
    fun observeContributions(period: Period): Flow<List<CategoryContribution>>
    fun observeAiSummary(period: Period): Flow<String>
    /** 가장 오래된 스냅샷의 timestamp (ms). 스냅샷이 없으면 null. */
    fun observeOldestTimestamp(): Flow<Long?>

    /** 가장 최근 스냅샷의 timestamp (ms). 스냅샷이 없으면 null. */
    fun observeNewestTimestamp(): Flow<Long?>

    /** 오늘 자정(시스템 TZ) 이전의 가장 최근 스냅샷. 없으면 null. */
    fun observeLatestBeforeToday(): Flow<SeriesPoint?>
}
