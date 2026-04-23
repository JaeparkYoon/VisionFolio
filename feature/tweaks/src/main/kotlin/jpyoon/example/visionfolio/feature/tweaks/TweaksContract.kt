package jpyoon.example.visionfolio.feature.tweaks

import jpyoon.example.visionfolio.core.android.ViewIntent
import jpyoon.example.visionfolio.core.android.ViewEffect
import jpyoon.example.visionfolio.core.android.ViewState
import jpyoon.example.visionfolio.domain.model.AccentPreset

data class TweaksState(
    val isOpen: Boolean = false,
    val accent: AccentPreset = AccentPreset.SALMON,
) : ViewState

sealed interface TweaksIntent : ViewIntent {
    object Open : TweaksIntent
    object Close : TweaksIntent
    data class SelectAccent(val preset: AccentPreset) : TweaksIntent
}

sealed interface TweaksEffect : ViewEffect {
    data class AccentChanged(val preset: AccentPreset) : TweaksEffect
}
