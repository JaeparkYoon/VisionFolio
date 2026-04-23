package jpyoon.example.visionfolio.feature.addholding.editlist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpyoon.example.visionfolio.core.android.MVIViewModel
import jpyoon.example.visionfolio.data.repository.HoldingRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditHoldingsViewModel @Inject constructor(
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
