package jpyoon.example.visionfolio.domain.model

enum class PaymentFrequency {
    MONTHLY,
    QUARTERLY,
    SEMI_ANNUAL,
    ANNUAL,
    UNKNOWN,
}

/**
 * API 로부터 받은 단일 배당 기록.
 */
data class DividendRecord(
    val stockName: String,
    val stockCode: String,
    val dividendPerShare: Double,
    val paymentDate: String,
    val currency: Currency,
)

/**
 * 보유 종목별 배당 정보.
 */
data class HoldingDividendInfo(
    val holdingId: String,
    val stockName: String,
    val stockCode: String,
    val quantity: Double,
    val dividendPerShare: Double,
    val annualDividendTotal: Double,
    val dividendYield: Double,
    val frequency: PaymentFrequency,
    val currency: Currency,
)

/**
 * 전체 포트폴리오 배당 요약.
 */
data class DividendSummary(
    val totalYearlyKrw: Long,
    val totalQuarterlyKrw: Long,
    val totalMonthlyKrw: Long,
    val holdings: List<HoldingDividendInfo>,
)
