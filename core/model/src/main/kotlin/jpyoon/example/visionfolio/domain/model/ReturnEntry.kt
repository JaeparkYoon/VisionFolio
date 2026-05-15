package jpyoon.example.visionfolio.domain.model

enum class ReturnCategory { SALARY, INVESTMENT, SIDE_INCOME, BONUS, OTHER }

data class ReturnEntry(
    val id: String,
    val year: Int,
    val month: Int,
    val category: ReturnCategory,
    val amount: Long,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)

data class ParsedReturnEntry(
    val year: Int,
    val month: Int,
    val category: ReturnCategory,
    val amount: Long,
    val note: String = "",
)

data class YtdSummary(
    val year: Int,
    val totalIncome: Long,
    val totalInvestment: Long,
    val savingsRate: Double,
    val monthlyBreakdown: List<MonthlyReturn>,
)

data class MonthlyReturn(
    val month: Int,
    val income: Long,
    val investment: Long,
)
