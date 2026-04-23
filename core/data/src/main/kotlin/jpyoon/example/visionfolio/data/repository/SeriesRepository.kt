package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.CategoryContribution
import jpyoon.example.visionfolio.domain.model.Period
import jpyoon.example.visionfolio.domain.model.PortfolioSeries
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {
    fun observeSeries(period: Period, customRange: ClosedRange<Long>? = null): Flow<PortfolioSeries>
    fun observeContributions(period: Period): Flow<List<CategoryContribution>>
    fun observeAiSummary(period: Period): Flow<String>
    /** 가장 오래된 스냅샷의 timestamp (ms). 스냅샷이 없으면 null. */
    fun observeOldestTimestamp(): Flow<Long?>
}
