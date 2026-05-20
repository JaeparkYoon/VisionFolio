package jpyoon.example.visionfolio.feature.trend

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import jpyoon.example.visionfolio.designsystem.di.injectedViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.core.analytics.event.LocalEventTracker
import jpyoon.example.visionfolio.feature.trend.component.CustomRangeSheet
import jpyoon.example.visionfolio.feature.trend.event.TrendEvents
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendRoute(
    viewModel: TrendViewModel = injectedViewModel(),
) {
    val eventTracker = LocalEventTracker.current
    val dispatcher = remember(viewModel) {
        TrendEventDispatcher(viewModel = viewModel, eventTracker = eventTracker)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showCustomSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        dispatcher.dispatchEvent(TrendEvents.ViewedTrend)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                TrendEffect.ShowCustomRangeSheet -> showCustomSheet = true
                TrendEffect.DismissCustomRangeSheet -> showCustomSheet = false
            }
        }
    }

    TrendScreen(state = state, onEvent = dispatcher::dispatchEvent)

    if (showCustomSheet) {
        CustomRangeSheet(
            sheetState = sheetState,
            onDismiss = {
                scope.launch {
                    sheetState.hide()
                    showCustomSheet = false
                }
            },
            onConfirm = { from, to ->
                dispatcher.dispatchEvent(TrendEvents.ClickedCustomRange(from, to))
                scope.launch {
                    sheetState.hide()
                    showCustomSheet = false
                }
            },
        )
    }
}
