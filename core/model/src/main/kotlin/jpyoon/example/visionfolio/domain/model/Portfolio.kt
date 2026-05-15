package jpyoon.example.visionfolio.domain.model

enum class AssetCategory {
    DOMESTIC_STOCK,
    OVERSEAS_STOCK,
    ETF,
    CRYPTO,
    BOND,
    CASH,
    PENSION,
    SAVINGS,
    OTHER,
}

val AssetCategory.isCash: Boolean get() = this == AssetCategory.CASH
val AssetCategory.isBond: Boolean get() = this == AssetCategory.BOND
val AssetCategory.isSavings: Boolean get() = this == AssetCategory.SAVINGS
val AssetCategory.isPension: Boolean get() = this == AssetCategory.PENSION
val AssetCategory.isOther: Boolean get() = this == AssetCategory.OTHER

enum class Currency { KRW, USD }

data class Holding(
    val id: String,
    val category: AssetCategory,
    val name: String,
    val code: String,
    val quantity: Double,
    val currentValue: Double,
    val currency: Currency,
    val maturityDate: String? = null,
    val source: String = "",
    val sector: Sector? = null,
    val excludedFromAllocation: Boolean = false,
)

data class EnrichedHolding(
    val holding: Holding,
    val valueKrw: Long,
)

data class PortfolioSummary(
    val totalValue: Long,
    val byCategory: Map<AssetCategory, Long>,
    val dayChange: Long,
    val dayPct: Double,
    val prevTimestamp: Long? = null,
)
