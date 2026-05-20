package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.Holding
import kotlinx.coroutines.flow.Flow

interface HoldingRepository {
    fun observe(): Flow<List<Holding>>
    suspend fun addAll(holdings: List<Holding>)
    suspend fun remove(id: String)
    suspend fun update(holding: Holding)
}
