package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.data.repository.DividendRepository
import jpyoon.example.visionfolio.data.repository.FxRateProvider
import jpyoon.example.visionfolio.data.repository.HoldingRepository
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.DividendSummary
import jpyoon.example.visionfolio.domain.model.HoldingDividendInfo
import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.domain.model.PaymentFrequency
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ObserveDividendData @Inject constructor(
    private val holdingRepo: HoldingRepository,
    private val dividendRepo: DividendRepository,
    private val fxProvider: FxRateProvider,
) {
    operator fun invoke(
        refreshTrigger: Flow<Unit> = flowOf(Unit),
    ): Flow<DividendSummary> = combine(
        holdingRepo.observe(),
        refreshTrigger.onStart { emit(Unit) },
    ) { holdings, _ -> holdings }
        .map { holdings ->
            val stockHoldings = holdings.filter { it.category.isDividendEligible }
            buildSummary(stockHoldings)
        }

    private suspend fun buildSummary(holdings: List<Holding>): DividendSummary = coroutineScope {
        val usdKrw = fxProvider.usdToKrw()

        val infos = holdings.map { holding ->
            async {
                val records = dividendRepo.getDividends(holding)
                if (records.isEmpty()) return@async null

                val latest = records.first()
                val frequency = inferFrequency(records.size)
                val paymentsPerYear = frequency.paymentsPerYear
                val annualDps = latest.dividendPerShare * paymentsPerYear
                val annualTotal = annualDps * holding.quantity
                val pricePerUnit = if (holding.quantity > 0) holding.currentValue / holding.quantity else 0.0
                val yield = if (pricePerUnit > 0) {
                    (annualDps / pricePerUnit) * 100.0
                } else 0.0

                HoldingDividendInfo(
                    holdingId = holding.id,
                    stockName = holding.name,
                    stockCode = holding.code,
                    quantity = holding.quantity,
                    dividendPerShare = latest.dividendPerShare,
                    annualDividendTotal = annualTotal,
                    dividendYield = yield,
                    frequency = frequency,
                    currency = latest.currency,
                )
            }
        }.awaitAll().filterNotNull()

        val totalYearlyKrw = infos.sumOf { info ->
            toKrw(info.annualDividendTotal, info.currency, usdKrw)
        }

        DividendSummary(
            totalYearlyKrw = totalYearlyKrw,
            totalQuarterlyKrw = totalYearlyKrw / 4,
            totalMonthlyKrw = totalYearlyKrw / 12,
            holdings = infos,
        )
    }

    private fun inferFrequency(recordCount: Int): PaymentFrequency = when {
        recordCount >= 12 -> PaymentFrequency.MONTHLY
        recordCount >= 4 -> PaymentFrequency.QUARTERLY
        recordCount >= 2 -> PaymentFrequency.SEMI_ANNUAL
        recordCount >= 1 -> PaymentFrequency.ANNUAL
        else -> PaymentFrequency.UNKNOWN
    }

    private fun toKrw(amount: Double, currency: Currency, usdKrw: Double): Long = when (currency) {
        Currency.KRW -> amount.toLong()
        Currency.USD -> (amount * usdKrw).toLong()
    }
}

private val PaymentFrequency.paymentsPerYear: Int
    get() = when (this) {
        PaymentFrequency.MONTHLY -> 12
        PaymentFrequency.QUARTERLY -> 4
        PaymentFrequency.SEMI_ANNUAL -> 2
        PaymentFrequency.ANNUAL -> 1
        PaymentFrequency.UNKNOWN -> 1
    }

private val AssetCategory.isDividendEligible: Boolean
    get() = this == AssetCategory.DOMESTIC_STOCK ||
            this == AssetCategory.OVERSEAS_STOCK ||
            this == AssetCategory.ETF
