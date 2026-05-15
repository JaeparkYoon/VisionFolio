package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.ReturnEntry
import jpyoon.example.visionfolio.domain.model.YtdSummary
import kotlinx.coroutines.flow.Flow

interface ReturnEntryRepository {
    fun observeByYear(year: Int): Flow<List<ReturnEntry>>
    fun observeAll(): Flow<List<ReturnEntry>>
    suspend fun upsert(entry: ReturnEntry)
    suspend fun delete(id: String)
    suspend fun getYtdSummary(year: Int): YtdSummary
}
