package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.csv.HoldingsCsv
import jpyoon.example.visionfolio.data.repository.HoldingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ExportHoldingsCsv @Inject constructor(
    private val holdingRepo: HoldingRepository,
) {
    suspend operator fun invoke(): String =
        HoldingsCsv.encode(holdingRepo.observe().first())
}

class ImportHoldingsCsv @Inject constructor(
    private val holdingRepo: HoldingRepository,
) {
    suspend operator fun invoke(text: String): Int {
        val holdings = HoldingsCsv.decode(text)
        holdingRepo.addAll(holdings)
        return holdings.size
    }
}
