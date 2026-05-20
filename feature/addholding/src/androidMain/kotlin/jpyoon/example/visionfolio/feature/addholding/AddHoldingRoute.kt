package jpyoon.example.visionfolio.feature.addholding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.core.analytics.event.LocalEventTracker
import jpyoon.example.visionfolio.designsystem.di.injectedViewModel
import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.feature.addholding.event.AddHoldingEvents

@Composable
fun AddHoldingRoute(
    onDismiss: () -> Unit,
    onCommitted: (name: String) -> Unit,
    editHolding: Holding? = null,
    viewModel: AddHoldingViewModel = injectedViewModel(),
) {
    val eventTracker = LocalEventTracker.current
    val dispatcher = remember(viewModel) {
        AddHoldingEventDispatcher(viewModel = viewModel, eventTracker = eventTracker)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(editHolding) {
        if (editHolding != null) viewModel.initForEdit(editHolding)
    }

    LaunchedEffect(Unit) {
        dispatcher.dispatchEvent(AddHoldingEvents.ViewedAddHolding)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                AddHoldingEffect.Close -> onDismiss()
                is AddHoldingEffect.Committed -> onCommitted(effect.name)
                is AddHoldingEffect.ShowError -> Unit
            }
        }
    }

    AddHoldingScreen(state = state, onIntent = viewModel::dispatch)
}
