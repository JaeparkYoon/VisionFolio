package jpyoon.example.visionfolio.domain.model

enum class ReturnCategory { STOCK, FUND, ETF, BOND, CRYPTO, SAVINGS, PENSION, REAL_ESTATE, OTHER }
data class ReturnEntry(val id: Long = 0, val category: ReturnCategory, val label: String, val investedAmount: Long, val currentValue: Long, val year: Int, val memo: String = "")
data class ParsedReturnEntry(val category: ReturnCategory, val label: String, val invested: Long, val current: Long)
data class YtdSummary(val totalInvested: Long, val totalCurrent: Long, val totalReturn: Long, val returnPct: Double)
