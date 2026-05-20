package jpyoon.example.visionfolio.data.billing

import jpyoon.example.visionfolio.core.repository.api.BillingRepository
import jpyoon.example.visionfolio.domain.model.credit.ProductInfo
import me.tatarka.inject.annotations.Inject

@Inject
class BillingRepositoryImpl : BillingRepository {
    override suspend fun getProducts(): List<ProductInfo> = emptyList()
    override suspend fun purchase(productId: String): Boolean = false
    override fun start() {}
    override fun stop() {}
}
