package jpyoon.example.visionfolio.domain.model.credit

data class CreditBalance(val remaining: Int, val firstGrantDone: Boolean = false)
data class CreditPack(val productId: String, val credits: Int, val priceLabel: String)
data class PurchaseResult(val success: Boolean, val creditsGranted: Int = 0, val message: String = "")
data class ProductInfo(val productId: String, val title: String, val price: String)
