package jpyoon.example.visionfolio.feature.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.designsystem.di.injectedViewModel

@Composable
fun ChatRoute(
    onBack: () -> Unit,
    viewModel: ChatViewModel = injectedViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ChatEffect.NavigateBack -> onBack()
                is ChatEffect.ShowError -> { /* Toast or Snackbar */ }
                ChatEffect.ShowCreditExhausted -> { /* Show credit exhausted dialog */ }
            }
        }
    }

    ChatScreen(
        state = state,
        onIntent = viewModel::dispatch,
        onBack = onBack,
    )
}
