package jpyoon.example.visionfolio.domain.usecase.returns

import jpyoon.example.visionfolio.data.repository.ReturnEntryRepository
import jpyoon.example.visionfolio.domain.model.YtdSummary
import javax.inject.Inject

class GetYtdSummaryUseCase @Inject constructor(
    private val repo: ReturnEntryRepository,
) {
    suspend operator fun invoke(year: Int): YtdSummary = repo.getYtdSummary(year)
}
