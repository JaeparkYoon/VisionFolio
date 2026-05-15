package jpyoon.example.visionfolio.feature.returns

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.designsystem.component.VfToastState
import jpyoon.example.visionfolio.designsystem.component.rememberVfToastState

@Composable
fun ReturnsRoute(
    onBack: () -> Unit,
    toastState: VfToastState = rememberVfToastState(),
    viewModel: ReturnsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ReturnsEffect.ShowError -> toastState.show(effect.message)
            }
        }
    }

    ReturnsScreen(
        state = state,
        onIntent = viewModel::dispatch,
        onBack = onBack,
    )
}
