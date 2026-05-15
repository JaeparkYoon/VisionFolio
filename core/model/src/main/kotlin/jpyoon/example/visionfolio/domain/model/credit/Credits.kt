package jpyoon.example.visionfolio.domain.model.credit

data class CreditBalance(
    val remaining: Int,
    val total: Int,
)

data class CreditPack(
    val id: String,
    val credits: Int,
    val price: String,
)

data class PurchaseResult(
    val success: Boolean,
    val newBalance: Int = 0,
)

data class ProductInfo(
    val packs: List<CreditPack>,
)
