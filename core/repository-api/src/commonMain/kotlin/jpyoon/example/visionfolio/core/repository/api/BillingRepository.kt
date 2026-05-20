package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.credit.CreditPack
import jpyoon.example.visionfolio.domain.model.credit.ProductInfo

interface BillingRepository {
    suspend fun getProducts(): List<ProductInfo>
    suspend fun purchase(productId: String): Boolean
    fun start()
    fun stop()
}
