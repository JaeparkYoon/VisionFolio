package jpyoon.example.visionfolio.domain.usecase.returns

import jpyoon.example.visionfolio.core.repository.api.ReturnEntryRepository
import jpyoon.example.visionfolio.domain.model.YtdSummary
import me.tatarka.inject.annotations.Inject

class GetYtdSummaryUseCase @Inject constructor(
    private val repository: ReturnEntryRepository,
) {
    suspend operator fun invoke(year: Int): YtdSummary = repository.getYtdSummary(year)
}
