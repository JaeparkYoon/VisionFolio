package jpyoon.example.visionfolio.domain.usecase.returns

import jpyoon.example.visionfolio.core.repository.api.ReturnEntryRepository
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

class ObserveReturnsUseCase @Inject constructor(
    private val repository: ReturnEntryRepository,
) {
    operator fun invoke(): Flow<List<ReturnEntry>> = repository.observeAll()
    fun byYear(year: Int): Flow<List<ReturnEntry>> = repository.observeByYear(year)
}
