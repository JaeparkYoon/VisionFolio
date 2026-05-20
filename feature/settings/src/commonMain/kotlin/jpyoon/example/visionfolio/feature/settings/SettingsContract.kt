package jpyoon.example.visionfolio.feature.settings

import jpyoon.example.visionfolio.core.common.ViewIntent
import jpyoon.example.visionfolio.core.common.ViewEffect
import jpyoon.example.visionfolio.core.common.ViewState
import jpyoon.example.visionfolio.domain.model.NotificationKey
import jpyoon.example.visionfolio.domain.model.NotificationSettings
import jpyoon.example.visionfolio.domain.model.UserProfile

data class SettingsState(
    val profile: UserProfile = UserProfile(),
    val holdingCount: Int = 0,
    val lastSyncAt: Long = 0L,
    val appVersion: String = "",
    val notifications: NotificationSettings = NotificationSettings(),
) : ViewState

sealed interface SettingsIntent : ViewIntent {
    object OpenEditAssets : SettingsIntent
    object OpenManageHoldings : SettingsIntent
    object OpenUpload : SettingsIntent
    object RequestExport : SettingsIntent
    data class ToggleNotification(val key: NotificationKey) : SettingsIntent
    object OpenLicenses : SettingsIntent
    object OpenTerms : SettingsIntent
}

sealed interface SettingsEffect : ViewEffect {
    object NavigateToEditAssets : SettingsEffect
    object NavigateToManageHoldings : SettingsEffect
    object NavigateToUpload : SettingsEffect
    object NavigateToLicenses : SettingsEffect
    data class ShowToast(val message: String) : SettingsEffect
    data class ShareFile(val uri: String) : SettingsEffect
    data class OpenUrl(val url: String) : SettingsEffect
}
