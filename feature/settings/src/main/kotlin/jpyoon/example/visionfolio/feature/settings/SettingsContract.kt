package jpyoon.example.visionfolio.feature.settings

import jpyoon.example.visionfolio.core.android.ViewIntent
import jpyoon.example.visionfolio.core.android.ViewEffect
import jpyoon.example.visionfolio.core.android.ViewState
import jpyoon.example.visionfolio.domain.model.NotificationKey
import jpyoon.example.visionfolio.domain.model.NotificationPrefs
import jpyoon.example.visionfolio.domain.model.UserProfile

data class SettingsState(
    val profile: UserProfile = UserProfile(displayName = "", initial = ""),
    val holdingCount: Int = 0,
    val lastSyncAt: Long = 0L,
    val notifications: NotificationPrefs = NotificationPrefs(),
    val appVersion: String = "1.0.0",
) : ViewState

sealed interface SettingsIntent : ViewIntent {
    object OpenEditAssets : SettingsIntent
    object OpenManageHoldings : SettingsIntent
    object OpenUpload : SettingsIntent
    data class ToggleNotification(val key: NotificationKey) : SettingsIntent
    object OpenLicenses : SettingsIntent
    object OpenTerms : SettingsIntent
    object RequestExport : SettingsIntent
    data class ImportFromText(val text: String) : SettingsIntent
}

sealed interface SettingsEffect : ViewEffect {
    object NavigateToEditAssets : SettingsEffect
    object NavigateToManageHoldings : SettingsEffect
    object NavigateToUpload : SettingsEffect
    object NavigateToLicenses : SettingsEffect
    object NavigateToTerms : SettingsEffect
    data class ExportReady(val csv: String) : SettingsEffect
    data class ImportCompleted(val count: Int) : SettingsEffect
    data class ShowError(val message: String) : SettingsEffect
}
