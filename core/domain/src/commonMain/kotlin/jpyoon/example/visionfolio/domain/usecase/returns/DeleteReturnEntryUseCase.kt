package jpyoon.example.visionfolio.domain.usecase.returns

import jpyoon.example.visionfolio.core.repository.api.ReturnEntryRepository
import me.tatarka.inject.annotations.Inject

class DeleteReturnEntryUseCase @Inject constructor(
    private val repository: ReturnEntryRepository,
) {
    suspend operator fun invoke(id: Long) = repository.delete(id)
}
