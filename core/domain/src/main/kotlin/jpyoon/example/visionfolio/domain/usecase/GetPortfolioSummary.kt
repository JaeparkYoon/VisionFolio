package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.PortfolioSummary
import kotlin.math.roundToLong

object GetPortfolioSummary {

    fun invoke(
        enriched: List<EnrichedHolding>,
        previousValue: Long? = null,
        prevTimestamp: Long? = null,
    ): PortfolioSummary {
        val totalValue = enriched.sumOf { it.valueKrw }
        val byCategory: Map<AssetCategory, Long> = enriched
            .filter { !it.holding.excludedFromAllocation }
            .groupBy { it.holding.category }
            .mapValues { (_, list) -> list.sumOf { it.valueKrw } }

        val dayChange: Long
        val dayPct: Double
        if (previousValue != null && previousValue != 0L) {
            dayChange = totalValue - previousValue
            dayPct = dayChange.toDouble() / previousValue.toDouble() * 100.0
        } else {
            dayPct = if (totalValue == 0L) 0.0 else 0.81
            dayChange = (totalValue * dayPct / 100.0).roundToLong()
        }

        return PortfolioSummary(
            totalValue = totalValue,
            byCategory = byCategory,
            dayChange = dayChange,
            dayPct = dayPct,
            prevTimestamp = prevTimestamp,
        )
    }
}
