package jpyoon.example.visionfolio.domain.usecase.returns

import jpyoon.example.visionfolio.data.repository.ReturnEntryRepository
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveReturnsUseCase @Inject constructor(
    private val repo: ReturnEntryRepository,
) {
    operator fun invoke(year: Int): Flow<List<ReturnEntry>> = repo.observeByYear(year)
}
