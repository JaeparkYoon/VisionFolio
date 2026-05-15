package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.DividendOverride
import kotlinx.coroutines.flow.Flow

interface DividendOverrideRepository {
    fun observeAll(): Flow<List<DividendOverride>>
    suspend fun upsert(override: DividendOverride)
    suspend fun delete(holdingId: String)
}
