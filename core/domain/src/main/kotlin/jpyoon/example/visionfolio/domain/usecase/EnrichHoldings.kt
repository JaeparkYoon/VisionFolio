package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.Holding
import kotlin.math.roundToLong

object EnrichHoldings {

    fun invoke(holdings: List<Holding>, usdKrw: Double): List<EnrichedHolding> =
        holdings.map { h ->
            val fx = if (h.currency == Currency.USD) usdKrw else 1.0
            val value = (h.currentValue * fx).roundToLong()
            EnrichedHolding(
                holding = h,
                valueKrw = value,
            )
        }
}
