package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.MarketIndex
import jpyoon.example.visionfolio.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    fun observeIndices(): Flow<List<MarketIndex>>
    fun observeNews(): Flow<List<NewsItem>>
    suspend fun refresh(force: Boolean = false)
    suspend fun setIndexOrder(names: List<String>)
}
