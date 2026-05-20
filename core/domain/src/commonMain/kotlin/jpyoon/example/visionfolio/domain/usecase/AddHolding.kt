package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import me.tatarka.inject.annotations.Inject

@OptIn(ExperimentalUuidApi::class)
class AddHolding @Inject constructor(
    private val holdingRepo: HoldingRepository,
) {
    suspend operator fun invoke(draft: HoldingDraft): Holding {
        val holding = Holding(
            id = draft.id ?: "manual-${Uuid.random()}",
            category = draft.category,
            name = draft.name.trim(),
            code = draft.code.trim(),
            quantity = draft.quantity,
            currentValue = draft.quantity * draft.currentPrice,
            currency = draft.currency,
            maturityDate = draft.maturityDate,
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
    val currentPrice: Double,
    val currency: jpyoon.example.visionfolio.domain.model.Currency,
    val maturityDate: String? = null,
)
