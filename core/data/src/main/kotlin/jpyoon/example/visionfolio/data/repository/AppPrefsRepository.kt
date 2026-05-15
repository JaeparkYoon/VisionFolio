package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.domain.model.AppPrefs
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.NotificationKey
import jpyoon.example.visionfolio.domain.model.NotificationPrefs
import jpyoon.example.visionfolio.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface AppPrefsRepository {
    fun observePrefs(): Flow<AppPrefs>
    fun observeNotifications(): Flow<NotificationPrefs>
    suspend fun setAccent(preset: AccentPreset)
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setHideAmounts(hide: Boolean)
    suspend fun setDisplayCurrency(currency: Currency)
    suspend fun toggleNotification(key: NotificationKey)
}
