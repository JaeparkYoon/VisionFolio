package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import jpyoon.example.visionfolio.core.repository.api.ReturnEntryRepository
import me.tatarka.inject.annotations.Inject

class ImportPortfolioCsvUseCase @Inject constructor(
    private val holdingRepository: HoldingRepository,
    private val returnEntryRepository: ReturnEntryRepository,
) {
    suspend operator fun invoke(csv: String): Result<Int> = runCatching { 0 }
}
