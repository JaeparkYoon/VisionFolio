package jpyoon.example.visionfolio.domain.usecase.returns

import jpyoon.example.visionfolio.data.repository.ReturnEntryRepository
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import javax.inject.Inject

class UpsertReturnEntryUseCase @Inject constructor(
    private val repo: ReturnEntryRepository,
) {
    suspend operator fun invoke(entry: ReturnEntry) = repo.upsert(entry)
}
