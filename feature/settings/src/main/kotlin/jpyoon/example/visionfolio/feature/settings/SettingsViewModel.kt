package jpyoon.example.visionfolio.feature.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpyoon.example.visionfolio.core.android.MVIViewModel
import jpyoon.example.visionfolio.domain.model.UserProfile
import jpyoon.example.visionfolio.data.repository.AppPrefsRepository
import jpyoon.example.visionfolio.data.repository.HoldingRepository
import jpyoon.example.visionfolio.domain.usecase.ExportHoldingsCsv
import jpyoon.example.visionfolio.domain.usecase.ImportHoldingsCsv
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPrefsRepo: AppPrefsRepository,
    private val holdingRepo: HoldingRepository,
    private val exportCsv: ExportHoldingsCsv,
    private val importCsv: ImportHoldingsCsv,
) : MVIViewModel<SettingsIntent, SettingsState, SettingsEffect>() {

    override fun createInitialState(): SettingsState =
        SettingsState(profile = UserProfile(displayName = "나", initial = "나"))

    init {
        appPrefsRepo.observeNotifications().onEach { prefs ->
            setState { copy(notifications = prefs) }
        }.launchIn(viewModelScope)

        appPrefsRepo.observePrefs().onEach { prefs ->
            setState { copy(lastSyncAt = prefs.lastSyncAt, appVersion = prefs.version) }
        }.launchIn(viewModelScope)

        holdingRepo.observe().onEach { holdings ->
            setState { copy(holdingCount = holdings.size) }
        }.launchIn(viewModelScope)
    }

    override suspend fun processIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.OpenEditAssets -> setEffect { SettingsEffect.NavigateToEditAssets }
            SettingsIntent.OpenManageHoldings -> setEffect { SettingsEffect.NavigateToManageHoldings }
            SettingsIntent.OpenUpload -> setEffect { SettingsEffect.NavigateToUpload }
            SettingsIntent.OpenLicenses -> setEffect { SettingsEffect.NavigateToLicenses }
            SettingsIntent.OpenTerms -> setEffect { SettingsEffect.NavigateToTerms }
            is SettingsIntent.ToggleNotification -> appPrefsRepo.toggleNotification(intent.key)
            SettingsIntent.RequestExport -> exportNow()
            is SettingsIntent.ImportFromText -> importNow(intent.text)
        }
    }

    private fun exportNow() {
        viewModelScope.launch {
            runCatching { exportCsv() }
                .onSuccess { csv -> setEffect { SettingsEffect.ExportReady(csv) } }
                .onFailure { t -> setEffect { SettingsEffect.ShowError(t.message ?: "내보내기 실패") } }
        }
    }

    private fun importNow(text: String) {
        viewModelScope.launch {
            runCatching { importCsv(text) }
                .onSuccess { count -> setEffect { SettingsEffect.ImportCompleted(count) } }
                .onFailure { t -> setEffect { SettingsEffect.ShowError(t.message ?: "불러오기 실패") } }
        }
    }
}
