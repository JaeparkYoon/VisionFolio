package jpyoon.example.visionfolio.domain.model.compute

import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.PortfolioSummary

object GetPortfolioSummary {

    fun invoke(
        enriched: List<EnrichedHolding>,
        previousValue: Long? = null,
        previousTimestamp: Long? = null,
    ): PortfolioSummary {
        val totalValue = enriched.sumOf { it.valueKrw }
        val byCategory: Map<AssetCategory, Long> = enriched
            .groupBy { it.holding.category }
            .mapValues { (_, list) -> list.sumOf { it.valueKrw } }

        val dayChange = if (previousValue != null) totalValue - previousValue else 0L
        val dayPct = if (previousValue != null && previousValue != 0L) {
            dayChange.toDouble() / previousValue.toDouble() * 100.0
        } else 0.0

        return PortfolioSummary(
            totalValue = totalValue,
            byCategory = byCategory,
            dayChange = dayChange,
            dayPct = dayPct,
            prevTimestamp = previousTimestamp ?: 0,
        )
    }
}
