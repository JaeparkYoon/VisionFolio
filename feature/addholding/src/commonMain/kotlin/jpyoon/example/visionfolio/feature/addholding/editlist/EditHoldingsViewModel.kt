package jpyoon.example.visionfolio.feature.addholding.editlist

import androidx.lifecycle.viewModelScope
import jpyoon.example.visionfolio.core.common.MVIViewModel
import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class EditHoldingsViewModel(
    private val holdingRepo: HoldingRepository,
) : MVIViewModel<EditHoldingsIntent, EditHoldingsState, EditHoldingsEffect>() {

    override fun createInitialState(): EditHoldingsState = EditHoldingsState()

    init {
        holdingRepo.observe().onEach { holdings ->
            setState { copy(holdings = holdings) }
        }.launchIn(viewModelScope)
    }

    override suspend fun processIntent(intent: EditHoldingsIntent) {
        when (intent) {
            is EditHoldingsIntent.Delete -> {
                viewModelScope.launch {
                    runCatching { holdingRepo.remove(intent.id) }
                        .onSuccess { setEffect { EditHoldingsEffect.ShowToast("삭제했어요") } }
                        .onFailure { setEffect { EditHoldingsEffect.ShowToast("삭제 실패") } }
                }
            }
            is EditHoldingsIntent.StartEdit -> setState { copy(editingHolding = intent.holding) }
            EditHoldingsIntent.DismissEdit -> setState { copy(editingHolding = null) }
        }
    }
}
