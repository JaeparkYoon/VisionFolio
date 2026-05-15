package jpyoon.example.visionfolio.domain.model.compute

import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.Holding
import kotlin.math.roundToLong

object EnrichHoldings {

    fun invoke(
        holdings: List<Holding>,
        usdKrw: Double,
        displayCurrency: Currency = Currency.KRW,
    ): List<EnrichedHolding> =
        holdings.map { h ->
            val fx = when (displayCurrency) {
                Currency.KRW -> if (h.currency == Currency.USD) usdKrw else 1.0
                Currency.USD -> if (h.currency == Currency.KRW) 1.0 / usdKrw else 1.0
            }
            val value = (h.currentValue * fx).roundToLong()
            EnrichedHolding(
                holding = h,
                valueKrw = value,
            )
        }
}
