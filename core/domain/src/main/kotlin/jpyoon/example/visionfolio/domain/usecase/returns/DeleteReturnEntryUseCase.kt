package jpyoon.example.visionfolio.domain.usecase.returns

import jpyoon.example.visionfolio.data.repository.ReturnEntryRepository
import javax.inject.Inject

class DeleteReturnEntryUseCase @Inject constructor(
    private val repo: ReturnEntryRepository,
) {
    suspend operator fun invoke(id: String) = repo.delete(id)
}
