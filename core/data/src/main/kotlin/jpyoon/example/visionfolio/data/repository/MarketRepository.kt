package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.MarketIndex
import jpyoon.example.visionfolio.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface MarketRepository {
    fun observeIndices(): Flow<List<MarketIndex>>
    fun observeNews(): Flow<List<NewsItem>>
    suspend fun refresh(force: Boolean = false)
}
