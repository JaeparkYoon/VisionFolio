package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.CategoryContribution
import jpyoon.example.visionfolio.domain.model.IntervalStats
import jpyoon.example.visionfolio.domain.model.Period
import jpyoon.example.visionfolio.domain.model.PortfolioSeries
import jpyoon.example.visionfolio.core.repository.api.SeriesRepository
import jpyoon.example.visionfolio.domain.model.compute.ComputeIntervalStats
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

data class TrendData(
    val series: PortfolioSeries,
    val stats: IntervalStats?,
    val contributions: List<CategoryContribution>,
    val aiSummary: String,
)

class ObserveTrendData @Inject constructor(
    private val seriesRepo: SeriesRepository,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(
        periodFlow: Flow<Period>,
        customRangeFlow: Flow<ClosedRange<Long>?> = flowOf(null),
    ): Flow<TrendData> = combine(periodFlow, customRangeFlow) { period, range -> period to range }
        .flatMapLatest { (period, range) ->
            combine(
                seriesRepo.observeSeries(period, range),
                seriesRepo.observeContributions(period),
                seriesRepo.observeAiSummary(period),
            ) { series, contributions, aiSummary ->
                TrendData(
                    series = series,
                    stats = ComputeIntervalStats.invoke(series),
                    contributions = contributions,
                    aiSummary = aiSummary,
                )
            }
        }
}
