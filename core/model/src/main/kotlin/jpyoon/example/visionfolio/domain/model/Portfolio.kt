package jpyoon.example.visionfolio.domain.model

enum class AssetCategory {
    DOMESTIC_STOCK,
    OVERSEAS_STOCK,
    ETF,
    CRYPTO,
    BOND,
    CASH,
    PENSION,
}

val AssetCategory.isCash: Boolean get() = this == AssetCategory.CASH
val AssetCategory.isBond: Boolean get() = this == AssetCategory.BOND

enum class Currency { KRW, USD }

data class Holding(
    val id: String,
    val category: AssetCategory,
    val name: String,
    val code: String,
    val quantity: Double,
    val avgPrice: Double,
    val currentPrice: Double,
    val currency: Currency,
    val maturityDate: String? = null,
    val source: String = "",
)

data class EnrichedHolding(
    val holding: Holding,
    val valueKrw: Long,
    val costKrw: Long,
    val plKrw: Long,
    val plPct: Double,
)

data class PortfolioSummary(
    val totalValue: Long,
    val totalCost: Long,
    val totalPl: Long,
    val totalPlPct: Double,
    val byCategory: Map<AssetCategory, Long>,
    val todayChange: Long,
    val todayPct: Double,
)
