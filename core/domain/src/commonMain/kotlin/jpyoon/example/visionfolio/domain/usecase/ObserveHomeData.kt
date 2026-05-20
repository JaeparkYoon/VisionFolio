package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.domain.model.PortfolioSummary
import jpyoon.example.visionfolio.domain.model.Quote
import jpyoon.example.visionfolio.core.repository.api.AppPrefsRepository
import jpyoon.example.visionfolio.core.repository.api.FxRateProvider
import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import jpyoon.example.visionfolio.core.repository.api.QuoteRepository
import jpyoon.example.visionfolio.domain.model.compute.EnrichHoldings
import jpyoon.example.visionfolio.domain.model.compute.GetPortfolioSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

data class HomeData(
    val quote: Quote,
    val summary: PortfolioSummary,
    val holdings: List<EnrichedHolding>,
    val displayCurrency: Currency,
)

class ObserveHomeData @Inject constructor(
    private val holdingRepo: HoldingRepository,
    private val quoteRepo: QuoteRepository,
    private val fxProvider: FxRateProvider,
    private val appPrefsRepo: AppPrefsRepository,
) {
    operator fun invoke(): Flow<HomeData> {
        val fxFlow = flow { emit(fxProvider.usdToKrw()) }
        val currencyFlow = appPrefsRepo.observePrefs().map { it.displayCurrency }
        val enrichedFlow = combine(holdingRepo.observe(), fxFlow, currencyFlow) { holdings, usdKrw, currency ->
            val merged = mergeHoldingsBySameAsset(holdings)
            val enriched = EnrichHoldings.invoke(merged, usdKrw, currency)
                .sortedByDescending { it.valueKrw }
            Triple(enriched, GetPortfolioSummary.invoke(enriched), currency)
        }

        return combine(
            enrichedFlow,
            quoteRepo.observeToday(),
        ) { (enriched, summary, currency), quote ->
            HomeData(
                quote = quote,
                summary = summary,
                holdings = enriched,
                displayCurrency = currency,
            )
        }
    }

    private fun mergeHoldingsBySameAsset(holdings: List<Holding>): List<Holding> {
        val (withCode, withoutCode) = holdings.partition { it.code.isNotBlank() }
        val mergedByCode = withCode.groupBy { it.code }.map { (_, group) ->
            if (group.size == 1) return@map group.first()
            val totalQty = group.sumOf { it.quantity }
            val weightedAvg = if (totalQty > 0) {
                group.sumOf { it.quantity * it.avgPrice } / totalQty
            } else 0.0
            group.first().copy(
                id = group.first().id,
                quantity = totalQty,
                avgPrice = weightedAvg,
                currentPrice = group.maxByOrNull { it.currentPrice }?.currentPrice ?: group.first().currentPrice,
                source = "",
            )
        }
        return mergedByCode + withoutCode
    }
}
