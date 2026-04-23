package jpyoon.example.visionfolio.feature.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jpyoon.example.visionfolio.core.analytics.event.LocalEventTracker
import jpyoon.example.visionfolio.designsystem.component.VfToastState
import jpyoon.example.visionfolio.designsystem.component.rememberVfToastState
import jpyoon.example.visionfolio.feature.settings.event.SettingsEvents

@Composable
fun SettingsRoute(
    onOpenUpload: () -> Unit,
    onOpenAddHolding: () -> Unit,
    onOpenManageHoldings: () -> Unit,
    onOpenLicenses: () -> Unit,
    toastState: VfToastState = rememberVfToastState(),
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val eventTracker = LocalEventTracker.current
    val dispatcher = remember(viewModel) {
        SettingsEventDispatcher(viewModel = viewModel, eventTracker = eventTracker)
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var pendingExport by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        dispatcher.dispatchEvent(SettingsEvents.ViewedSettings)
    }

    val createDocLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        val csv = pendingExport
        pendingExport = null
        if (uri != null && csv != null) {
            runCatching {
                context.contentResolver.openOutputStream(uri)?.use { it.write(csv.toByteArray()) }
            }.onSuccess { toastState.show("CSV로 내보냈어요") }
                .onFailure { toastState.show("내보내기 실패") }
        }
    }

    val openDocLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val text = runCatching {
                context.contentResolver.openInputStream(uri)?.use { it.reader().readText() }
            }.getOrNull()
            if (text != null) viewModel.dispatch(SettingsIntent.ImportFromText(text))
            else toastState.show("불러오기 실패")
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingsEffect.NavigateToEditAssets -> onOpenAddHolding()
                SettingsEffect.NavigateToManageHoldings -> onOpenManageHoldings()
                SettingsEffect.NavigateToUpload -> onOpenUpload()
                SettingsEffect.NavigateToLicenses -> onOpenLicenses()
                SettingsEffect.NavigateToTerms -> Unit
                is SettingsEffect.ExportReady -> {
                    pendingExport = effect.csv
                    createDocLauncher.launch("visionfolio_holdings.csv")
                }
                is SettingsEffect.ImportCompleted -> toastState.show("${effect.count}개 자산을 불러왔어요")
                is SettingsEffect.ShowError -> toastState.show(effect.message)
            }
        }
    }

    SettingsScreen(
        state = state,
        onIntent = viewModel::dispatch,
        onOpenImport = {
            dispatcher.dispatchEvent(SettingsEvents.ClickedImport)
            openDocLauncher.launch(arrayOf("text/csv", "text/comma-separated-values", "text/*"))
        },
    )
}
