package jpyoon.example.visionfolio.feature.tweaks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.core.analytics.event.LocalEventTracker
import jpyoon.example.visionfolio.designsystem.di.injectedViewModel
import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.feature.tweaks.event.TweaksEvents

@Composable
fun TweaksRoute(
    onClose: () -> Unit,
    onAccentChanged: (AccentPreset) -> Unit = {},
    viewModel: TweaksViewModel = injectedViewModel(),
) {
    val eventTracker = LocalEventTracker.current
    val dispatcher = remember(viewModel) {
        TweaksEventDispatcher(viewModel = viewModel, eventTracker = eventTracker)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dispatcher.dispatchEvent(TweaksEvents.ViewedTweaks)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TweaksEffect.AccentChanged -> onAccentChanged(effect.preset)
            }
        }
    }

    TweaksScreen(
        state = state,
        onIntent = viewModel::dispatch,
        onClose = onClose,
    )
}
