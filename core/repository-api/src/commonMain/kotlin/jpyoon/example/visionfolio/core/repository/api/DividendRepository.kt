package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.DividendRecord
import jpyoon.example.visionfolio.domain.model.Holding
import kotlinx.coroutines.flow.Flow

interface DividendRepository {
    suspend fun getDividends(holding: Holding): List<DividendRecord>
    suspend fun clearCache()
    fun observeCachedHoldingIds(): Flow<Set<String>>
}
