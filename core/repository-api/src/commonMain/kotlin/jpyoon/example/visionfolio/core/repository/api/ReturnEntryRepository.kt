package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.ReturnEntry
import jpyoon.example.visionfolio.domain.model.YtdSummary
import kotlinx.coroutines.flow.Flow

interface ReturnEntryRepository {
    fun observeAll(): Flow<List<ReturnEntry>>
    fun observeByYear(year: Int): Flow<List<ReturnEntry>>
    suspend fun upsert(entry: ReturnEntry)
    suspend fun delete(id: Long)
    suspend fun getYtdSummary(year: Int): YtdSummary
}
