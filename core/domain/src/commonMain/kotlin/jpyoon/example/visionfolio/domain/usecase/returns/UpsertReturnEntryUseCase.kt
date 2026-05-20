package jpyoon.example.visionfolio.domain.usecase.returns

import jpyoon.example.visionfolio.core.repository.api.ReturnEntryRepository
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import me.tatarka.inject.annotations.Inject

class UpsertReturnEntryUseCase @Inject constructor(
    private val repository: ReturnEntryRepository,
) {
    suspend operator fun invoke(entry: ReturnEntry) = repository.upsert(entry)
}
