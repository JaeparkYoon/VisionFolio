package jpyoon.example.visionfolio.feature.dividend

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.core.analytics.event.LocalEventTracker
import jpyoon.example.visionfolio.feature.dividend.event.DividendEvents

@Composable
fun DividendRoute(
    onOpenGuru: (String) -> Unit,
    viewModel: DividendViewModel = hiltViewModel(),
) {
    val eventTracker = LocalEventTracker.current
    val dispatcher = remember(viewModel) {
        DividendEventDispatcher(viewModel = viewModel, eventTracker = eventTracker)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dispatcher.dispatchEvent(DividendEvents.ViewedDividend)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DividendEffect.NavigateToGuru -> onOpenGuru(effect.guruId)
                is DividendEffect.NavigateToNews -> Unit
            }
        }
    }

    DividendScreen(state = state, onEvent = dispatcher::dispatchEvent)
}
