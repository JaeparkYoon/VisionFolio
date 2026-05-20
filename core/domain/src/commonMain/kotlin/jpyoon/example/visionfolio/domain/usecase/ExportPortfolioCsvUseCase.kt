package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import jpyoon.example.visionfolio.core.repository.api.ReturnEntryRepository
import me.tatarka.inject.annotations.Inject

class ExportPortfolioCsvUseCase @Inject constructor(
    private val holdingRepository: HoldingRepository,
    private val returnEntryRepository: ReturnEntryRepository,
) {
    suspend operator fun invoke(): String {
        // Mock: return empty CSV
        return "id,category,name,code,quantity,currentValue,currency\n"
    }
}
