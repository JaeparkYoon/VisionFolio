package jpyoon.example.visionfolio.data.portfolio

import jpyoon.example.visionfolio.core.repository.api.ReturnEntryRepository
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import jpyoon.example.visionfolio.domain.model.YtdSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ReturnEntryRepositoryImpl : ReturnEntryRepository {
    private val entries = MutableStateFlow<List<ReturnEntry>>(emptyList())
    private var nextId = 1L

    override fun observeAll(): Flow<List<ReturnEntry>> = entries

    override fun observeByYear(year: Int): Flow<List<ReturnEntry>> =
        entries.map { list -> list.filter { it.year == year } }

    override suspend fun upsert(entry: ReturnEntry) {
        val e = if (entry.id == 0L) entry.copy(id = nextId++) else entry
        entries.value = entries.value.filter { it.id != e.id } + e
    }

    override suspend fun delete(id: Long) {
        entries.value = entries.value.filter { it.id != id }
    }

    override suspend fun getYtdSummary(year: Int): YtdSummary {
        val yearEntries = entries.value.filter { it.year == year }
        val totalInvested = yearEntries.sumOf { it.investedAmount }
        val totalCurrent = yearEntries.sumOf { it.currentValue }
        val totalReturn = totalCurrent - totalInvested
        val pct = if (totalInvested > 0) totalReturn.toDouble() / totalInvested * 100 else 0.0
        return YtdSummary(totalInvested, totalCurrent, totalReturn, pct)
    }
}
