package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.Holding
import kotlin.math.roundToLong

object EnrichHoldings {

    fun invoke(holdings: List<Holding>, usdKrw: Double): List<EnrichedHolding> =
        holdings.map { h ->
            val fx = if (h.currency == Currency.USD) usdKrw else 1.0
            val value = (h.quantity * h.currentPrice * fx).roundToLong()
            val cost = (h.quantity * h.avgPrice * fx).roundToLong()
            val pl = value - cost
            val plPct = if (cost == 0L) 0.0 else (pl.toDouble() / cost.toDouble()) * 100.0
            EnrichedHolding(
                holding = h,
                valueKrw = value,
                costKrw = cost,
                plKrw = pl,
                plPct = plPct,
            )
        }
}
