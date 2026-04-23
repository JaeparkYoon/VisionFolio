package jpyoon.example.visionfolio.feature.dividend

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpyoon.example.visionfolio.core.android.MVIViewModel
import jpyoon.example.visionfolio.data.repository.MarketRepository
import jpyoon.example.visionfolio.domain.model.GuruData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DividendViewModel @Inject constructor(
    private val marketRepo: MarketRepository,
) : MVIViewModel<DividendIntent, DividendState, DividendEffect>() {

    override fun createInitialState(): DividendState = DividendState(isLoading = true)

    init {
        setState { copy(gurus = GuruData.profiles) }
        launch { marketRepo.refresh() }

        marketRepo.observeIndices().onEach { indices ->
            setState { copy(isLoading = false, indices = indices) }
        }.launchIn(viewModelScope)

        marketRepo.observeNews().onEach { news ->
            setState { copy(news = news) }
        }.launchIn(viewModelScope)
    }

    override suspend fun processIntent(intent: DividendIntent) {
        when (intent) {
            DividendIntent.Refresh -> {
                setState { copy(isLoading = true) }
                launch { marketRepo.refresh() }
            }
            is DividendIntent.OpenNews -> Unit
            is DividendIntent.OpenIndex -> Unit
            is DividendIntent.OpenGuru -> setEffect { DividendEffect.NavigateToGuru(intent.guruId) }
        }
    }
}
