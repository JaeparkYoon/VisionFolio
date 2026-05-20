package jpyoon.example.visionfolio.feature.tweaks

import jpyoon.example.visionfolio.core.common.ViewIntent
import jpyoon.example.visionfolio.core.common.ViewEffect
import jpyoon.example.visionfolio.core.common.ViewState
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
