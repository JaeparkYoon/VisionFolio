package jpyoon.example.visionfolio.feature.settings

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
import jpyoon.example.visionfolio.designsystem.component.VfToastState
import jpyoon.example.visionfolio.feature.settings.event.SettingsEvents

@Composable
fun SettingsRoute(
    onOpenUpload: () -> Unit,
    onOpenAddHolding: () -> Unit,
    onOpenManageHoldings: () -> Unit,
    onOpenLicenses: () -> Unit,
    toastState: VfToastState,
    viewModel: SettingsViewModel = injectedViewModel(),
) {
    val context = LocalContext.current
    val eventTracker = LocalEventTracker.current
    val dispatcher = remember(viewModel) {
        SettingsEventDispatcher(viewModel = viewModel, eventTracker = eventTracker)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dispatcher.dispatchEvent(SettingsEvents.ViewedSettings)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingsEffect.NavigateToEditAssets -> onOpenAddHolding()
                SettingsEffect.NavigateToManageHoldings -> onOpenManageHoldings()
                SettingsEffect.NavigateToUpload -> onOpenUpload()
                SettingsEffect.NavigateToLicenses -> onOpenLicenses()
                is SettingsEffect.ShowToast -> toastState.show(effect.message)
                is SettingsEffect.ShareFile -> Unit
                is SettingsEffect.OpenUrl -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(effect.url)))
                }
            }
        }
    }

    SettingsScreen(
        state = state,
        onIntent = viewModel::dispatch,
        onOpenImport = {
            dispatcher.dispatchEvent(SettingsEvents.ClickedImport)
        },
    )
}
