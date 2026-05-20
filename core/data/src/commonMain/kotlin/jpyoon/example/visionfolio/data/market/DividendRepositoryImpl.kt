package jpyoon.example.visionfolio.data.market

import jpyoon.example.visionfolio.core.repository.api.DividendRepository
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.DividendRecord
import jpyoon.example.visionfolio.domain.model.Holding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

@AppSingleton
class DividendRepositoryImpl @Inject constructor() : DividendRepository {

    override suspend fun getDividends(holding: Holding): List<DividendRecord> {
        if (!holding.category.isDividendEligible) return emptyList()
        return FakeDividendData.forHolding(holding)
    }

    override suspend fun clearCache() = Unit

    override fun observeCachedHoldingIds(): Flow<Set<String>> = flowOf(emptySet())

    private val AssetCategory.isDividendEligible: Boolean
        get() = this == AssetCategory.DOMESTIC_STOCK ||
                this == AssetCategory.OVERSEAS_STOCK ||
                this == AssetCategory.ETF
}
