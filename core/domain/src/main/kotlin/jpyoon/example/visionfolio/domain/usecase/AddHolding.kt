package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.data.repository.HoldingRepository
import java.util.UUID
import javax.inject.Inject

class AddHolding @Inject constructor(
    private val holdingRepo: HoldingRepository,
) {
    suspend operator fun invoke(draft: HoldingDraft): Holding {
        val holding = Holding(
            id = draft.id ?: "manual-${UUID.randomUUID()}",
            category = draft.category,
            name = draft.name.trim(),
            code = draft.code.trim(),
            quantity = draft.quantity,
            currentValue = draft.currentValue,
            currency = draft.currency,
            maturityDate = draft.maturityDate,
            excludedFromAllocation = draft.excludedFromAllocation,
        )
        holdingRepo.addAll(listOf(holding))
        return holding
    }
}

data class HoldingDraft(
    val id: String? = null,
    val category: jpyoon.example.visionfolio.domain.model.AssetCategory,
    val name: String,
    val code: String,
    val quantity: Double,
    val currentValue: Double,
    val currency: jpyoon.example.visionfolio.domain.model.Currency,
    val maturityDate: String? = null,
    val excludedFromAllocation: Boolean = false,
)
