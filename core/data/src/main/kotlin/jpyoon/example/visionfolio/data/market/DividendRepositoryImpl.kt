package jpyoon.example.visionfolio.data.market

import jpyoon.example.visionfolio.data.repository.DividendRepository
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.DividendRecord
import jpyoon.example.visionfolio.domain.model.Holding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DividendRepositoryImpl @Inject constructor() : DividendRepository {

    override suspend fun getDividends(holding: Holding): List<DividendRecord> {
        if (!holding.category.isDividendEligible) return emptyList()
        return FakeDividendData.forHolding(holding)
    }

    override suspend fun clearCache() = Unit

    private val AssetCategory.isDividendEligible: Boolean
        get() = this == AssetCategory.DOMESTIC_STOCK ||
                this == AssetCategory.OVERSEAS_STOCK ||
                this == AssetCategory.ETF
}
