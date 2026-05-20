package jpyoon.example.visionfolio.feature.settings

import jpyoon.example.visionfolio.core.common.MVIViewModel
import jpyoon.example.visionfolio.core.repository.api.AppPrefsRepository
import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class SettingsViewModel @Inject constructor(
    private val holdingRepo: HoldingRepository,
    private val appPrefsRepo: AppPrefsRepository,
) : MVIViewModel<SettingsIntent, SettingsState, SettingsEffect>() {

    override fun createInitialState(): SettingsState = SettingsState()

    init {
        holdingRepo.observeCount().onEach { count ->
            setState { copy(holdingCount = count) }
        }.launchIn(viewModelScope)

        appPrefsRepo.observePrefs().onEach { prefs ->
            setState {
                copy(
                    profile = prefs.profile,
                    lastSyncAt = prefs.lastSyncAt,
                    appVersion = prefs.appVersion,
                    notifications = prefs.notifications,
                )
            }
        }.launchIn(viewModelScope)
    }

    override suspend fun processIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.OpenEditAssets -> setEffect { SettingsEffect.NavigateToEditAssets }
            SettingsIntent.OpenManageHoldings -> setEffect { SettingsEffect.NavigateToManageHoldings }
            SettingsIntent.OpenUpload -> setEffect { SettingsEffect.NavigateToUpload }
            SettingsIntent.RequestExport -> {
                setEffect { SettingsEffect.ShowToast("Export not yet implemented") }
            }
            is SettingsIntent.ToggleNotification -> {
                appPrefsRepo.toggleNotification(intent.key)
            }
            SettingsIntent.OpenLicenses -> setEffect { SettingsEffect.NavigateToLicenses }
            SettingsIntent.OpenTerms -> setEffect { SettingsEffect.OpenUrl("https://visionfolio.app/terms") }
        }
    }
}
