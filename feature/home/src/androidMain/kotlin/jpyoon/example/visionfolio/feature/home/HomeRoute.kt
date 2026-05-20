package jpyoon.example.visionfolio.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import jpyoon.example.visionfolio.designsystem.di.injectedViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.core.analytics.event.LocalEventTracker
import jpyoon.example.visionfolio.feature.home.event.HomeEvents
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeRoute(
    onOpenUpload: () -> Unit,
    onOpenHoldings: () -> Unit,
    onOpenTrend: () -> Unit,
    viewModel: HomeViewModel = injectedViewModel(),
) {
    val eventTracker = LocalEventTracker.current
    val dispatcher = remember(viewModel) {
        HomeEventDispatcher(viewModel = viewModel, eventTracker = eventTracker)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val todayLabel = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("M월 d일 EEEE", Locale.KOREAN))
    }

    LaunchedEffect(Unit) {
        dispatcher.dispatchEvent(HomeEvents.ViewedHome)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HomeEffect.NavigateToUpload -> onOpenUpload()
                HomeEffect.NavigateToHoldings -> onOpenHoldings()
                HomeEffect.NavigateToTrend -> onOpenTrend()
                is HomeEffect.ShowToast -> Unit
            }
        }
    }

    HomeScreen(
        state = state,
        onEvent = dispatcher::dispatchEvent,
        todayLabel = todayLabel,
    )
}
