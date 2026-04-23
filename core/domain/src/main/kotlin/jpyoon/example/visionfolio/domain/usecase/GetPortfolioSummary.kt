package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.PortfolioSummary
import kotlin.math.roundToLong

object GetPortfolioSummary {

    /**
     * enriched로부터 요약 계산.
     * todayChange/todayPct는 실제 시계열이 필요하므로 지금은 heuristic (총 평가액의 0.81%)로 대입.
     * 추후 TimeSeries 도입 시 이 부분을 교체.
     */
    fun invoke(enriched: List<EnrichedHolding>): PortfolioSummary {
        val totalValue = enriched.sumOf { it.valueKrw }
        val totalCost = enriched.sumOf { it.costKrw }
        val totalPl = totalValue - totalCost
        val totalPlPct = if (totalCost == 0L) 0.0 else totalPl.toDouble() / totalCost.toDouble() * 100.0
        val byCategory: Map<AssetCategory, Long> = enriched
            .groupBy { it.holding.category }
            .mapValues { (_, list) -> list.sumOf { it.valueKrw } }

        val todayPct = 0.81
        val todayChange = (totalValue * todayPct / 100.0).roundToLong()

        return PortfolioSummary(
            totalValue = totalValue,
            totalCost = totalCost,
            totalPl = totalPl,
            totalPlPct = totalPlPct,
            byCategory = byCategory,
            todayChange = todayChange,
            todayPct = todayPct,
        )
    }
}
