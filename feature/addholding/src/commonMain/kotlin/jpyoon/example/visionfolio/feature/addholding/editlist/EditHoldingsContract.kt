package jpyoon.example.visionfolio.feature.addholding.editlist

import jpyoon.example.visionfolio.core.common.ViewEffect
import jpyoon.example.visionfolio.core.common.ViewIntent
import jpyoon.example.visionfolio.core.common.ViewState
import jpyoon.example.visionfolio.domain.model.Holding

data class EditHoldingsState(
    val holdings: List<Holding> = emptyList(),
    val editingHolding: Holding? = null,
) : ViewState

sealed interface EditHoldingsIntent : ViewIntent {
    data class Delete(val id: String) : EditHoldingsIntent
    data class StartEdit(val holding: Holding) : EditHoldingsIntent
    object DismissEdit : EditHoldingsIntent
}

sealed interface EditHoldingsEffect : ViewEffect {
    data class ShowToast(val message: String) : EditHoldingsEffect
}
