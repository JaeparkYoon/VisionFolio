package jpyoon.example.visionfolio.feature.dividend

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import jpyoon.example.visionfolio.designsystem.di.injectedViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.core.analytics.event.LocalEventTracker
import jpyoon.example.visionfolio.feature.dividend.event.DividendEvents

@Composable
fun DividendRoute(
    onOpenGuru: (String) -> Unit,
    viewModel: DividendViewModel = injectedViewModel(),
) {
    val context = LocalContext.current
    val eventTracker = LocalEventTracker.current
    val dispatcher = remember(viewModel) {
        DividendEventDispatcher(viewModel = viewModel, eventTracker = eventTracker)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dispatcher.dispatchEvent(DividendEvents.ViewedDividend)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DividendEffect.NavigateToGuru -> onOpenGuru(effect.guruId)
            }
        }
    }

    DividendScreen(state = state, onEvent = dispatcher::dispatchEvent)
}
