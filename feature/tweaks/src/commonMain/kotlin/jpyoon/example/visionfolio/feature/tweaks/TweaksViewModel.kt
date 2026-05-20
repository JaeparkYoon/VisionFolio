package jpyoon.example.visionfolio.feature.tweaks

import androidx.lifecycle.viewModelScope
import jpyoon.example.visionfolio.core.common.MVIViewModel
import jpyoon.example.visionfolio.core.repository.api.AppPrefsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

@Inject
class TweaksViewModel(
    private val appPrefsRepo: AppPrefsRepository,
) : MVIViewModel<TweaksIntent, TweaksState, TweaksEffect>() {

    override fun createInitialState(): TweaksState = TweaksState()

    init {
        appPrefsRepo.observePrefs().onEach { prefs ->
            setState { copy(accent = prefs.accentPreset) }
        }.launchIn(viewModelScope)
    }

    override suspend fun processIntent(intent: TweaksIntent) {
        when (intent) {
            TweaksIntent.Open -> setState { copy(isOpen = true) }
            TweaksIntent.Close -> setState { copy(isOpen = false) }
            is TweaksIntent.SelectAccent -> {
                appPrefsRepo.setAccent(intent.preset)
                setEffect { TweaksEffect.AccentChanged(intent.preset) }
            }
        }
    }
}
