package jpyoon.example.visionfolio.feature.addholding.editlist

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.designsystem.component.VfToastState
import jpyoon.example.visionfolio.designsystem.component.rememberVfToastState
import jpyoon.example.visionfolio.feature.addholding.AddHoldingRoute
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHoldingsRoute(
    onDismiss: () -> Unit,
    toastState: VfToastState = rememberVfToastState(),
    viewModel: EditHoldingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val editSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is EditHoldingsEffect.ShowToast -> toastState.show(effect.message)
            }
        }
    }

    EditHoldingsScreen(
        state = state,
        onIntent = viewModel::dispatch,
        onBack = onDismiss,
    )

    val editingHolding = state.editingHolding
    if (editingHolding != null) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dispatch(EditHoldingsIntent.DismissEdit) },
            sheetState = editSheetState,
            containerColor = VfColors.Card,
        ) {
            AddHoldingRoute(
                onDismiss = {
                    scope.launch {
                        editSheetState.hide()
                        viewModel.dispatch(EditHoldingsIntent.DismissEdit)
                    }
                },
                onCommitted = { name ->
                    toastState.show("수정했어요")
                    scope.launch {
                        editSheetState.hide()
                        viewModel.dispatch(EditHoldingsIntent.DismissEdit)
                    }
                },
                editHolding = editingHolding,
            )
        }
    }
}
