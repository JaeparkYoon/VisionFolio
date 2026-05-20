package jpyoon.example.visionfolio.data.market

import jpyoon.example.visionfolio.domain.model.MarketIndex
import jpyoon.example.visionfolio.domain.model.NewsItem
import jpyoon.example.visionfolio.core.repository.api.MarketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

@AppSingleton
class MarketRepositoryImpl @Inject constructor() : MarketRepository {

    private val indicesState = MutableStateFlow(FakeMarketData.Indices)
    private val newsState = MutableStateFlow(FakeMarketData.News)

    override fun observeIndices(): Flow<List<MarketIndex>> = indicesState.asStateFlow()
    override fun observeNews(): Flow<List<NewsItem>> = newsState.asStateFlow()

    override suspend fun refresh(force: Boolean) = Unit

    override suspend fun setIndexOrder(names: List<String>) = Unit
}
