package jpyoon.example.visionfolio.feature.returns

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.designsystem.di.injectedViewModel

@Composable
fun ReturnsRoute(
    onBack: () -> Unit,
    viewModel: ReturnsViewModel = injectedViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ReturnsEffect.NavigateBack -> onBack()
                is ReturnsEffect.ShowError -> { /* Toast or Snackbar */ }
                ReturnsEffect.ShowSaved -> { /* Show saved confirmation */ }
            }
        }
    }

    ReturnsScreen(
        state = state,
        onIntent = viewModel::dispatch,
        onBack = onBack,
    )
}
