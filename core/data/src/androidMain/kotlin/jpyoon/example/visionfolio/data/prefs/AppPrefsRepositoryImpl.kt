package jpyoon.example.visionfolio.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.domain.model.AppPrefs
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.NotificationKey
import jpyoon.example.visionfolio.domain.model.NotificationPrefs
import jpyoon.example.visionfolio.domain.model.ThemeMode
import jpyoon.example.visionfolio.core.repository.api.AppPrefsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.UUID
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

@AppSingleton
class AppPrefsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AppPrefsRepository {

    override fun observePrefs(): Flow<AppPrefs> = dataStore.data.map { prefs ->
        AppPrefs(
            accentPreset = prefs[Keys.Accent]?.toAccentPreset() ?: AccentPreset.SALMON,
            themeMode = prefs[Keys.ThemeMode]?.toThemeMode() ?: ThemeMode.SYSTEM,
            hideAmounts = prefs[Keys.HideAmounts] ?: false,
            displayCurrency = prefs[Keys.DisplayCurrency]?.toCurrency() ?: Currency.KRW,
            lastSyncAt = prefs[Keys.LastSyncAt] ?: System.currentTimeMillis(),
            version = prefs[Keys.Version] ?: "1.0.0",
        )
    }

    override fun observeNotifications(): Flow<NotificationPrefs> = dataStore.data.map { prefs ->
        NotificationPrefs(
            dailySummary = prefs[Keys.NotifDaily] ?: true,
            headlineNews = prefs[Keys.NotifHeadline] ?: true,
            priceAlert = prefs[Keys.NotifPrice] ?: false,
        )
    }

    override suspend fun setAccent(preset: AccentPreset) {
        dataStore.edit { it[Keys.Accent] = preset.name }
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[Keys.ThemeMode] = mode.name }
    }

    override suspend fun setHideAmounts(hide: Boolean) {
        dataStore.edit { it[Keys.HideAmounts] = hide }
    }

    override suspend fun setDisplayCurrency(currency: Currency) {
        dataStore.edit { it[Keys.DisplayCurrency] = currency.name }
    }

    override suspend fun toggleNotification(key: NotificationKey) {
        dataStore.edit { prefs ->
            when (key) {
                NotificationKey.DAILY_SUMMARY -> prefs[Keys.NotifDaily] = !(prefs[Keys.NotifDaily] ?: true)
                NotificationKey.HEADLINE_NEWS -> prefs[Keys.NotifHeadline] = !(prefs[Keys.NotifHeadline] ?: true)
                NotificationKey.PRICE_ALERT -> prefs[Keys.NotifPrice] = !(prefs[Keys.NotifPrice] ?: false)
            }
        }
    }

    override fun observeNotifDenialCount(): Flow<Int> = dataStore.data.map { prefs ->
        prefs[Keys.NotifDenialCount] ?: 0
    }

    override suspend fun incrementNotifDenialCount() {
        dataStore.edit { prefs ->
            prefs[Keys.NotifDenialCount] = (prefs[Keys.NotifDenialCount] ?: 0) + 1
        }
    }

    override suspend fun resetNotifDenialCount() {
        dataStore.edit { prefs -> prefs[Keys.NotifDenialCount] = 0 }
    }

    override fun observeUserUuid(): Flow<String> = dataStore.data
        .map { it[Keys.UserUuid] }
        .distinctUntilChanged()
        .map { existing ->
            existing ?: dataStore.edit { prefs ->
                if (prefs[Keys.UserUuid] == null) {
                    prefs[Keys.UserUuid] = UUID.randomUUID().toString()
                }
            }[Keys.UserUuid]!!
        }

    override fun observeScreenshotGuideHidden(): Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[Keys.ScreenshotGuideHidden] ?: false
    }

    override suspend fun setScreenshotGuideHidden(hidden: Boolean) {
        dataStore.edit { it[Keys.ScreenshotGuideHidden] = hidden }
    }

    override fun observeReturnBaseline(year: Int): Flow<Long?> = dataStore.data.map { prefs ->
        prefs[longPreferencesKey("return_baseline_$year")]
    }

    override suspend fun setReturnBaseline(year: Int, krw: Long?) {
        val key = longPreferencesKey("return_baseline_$year")
        dataStore.edit { prefs ->
            if (krw == null) prefs.remove(key) else prefs[key] = krw
        }
    }

    private object Keys {
        val Accent = stringPreferencesKey("accent_preset")
        val ThemeMode = stringPreferencesKey("theme_mode")
        val HideAmounts = booleanPreferencesKey("hide_amounts")
        val DisplayCurrency = stringPreferencesKey("display_currency")
        val LastSyncAt = longPreferencesKey("last_sync_at")
        val Version = stringPreferencesKey("version")
        val NotifDaily = booleanPreferencesKey("notif_daily")
        val NotifHeadline = booleanPreferencesKey("notif_headline")
        val NotifPrice = booleanPreferencesKey("notif_price")
        val NotifDenialCount = intPreferencesKey("notif_denial_count")
        val UserUuid = stringPreferencesKey("user_uuid")
        val ScreenshotGuideHidden = booleanPreferencesKey("screenshot_guide_hidden")
    }
}

private fun String.toAccentPreset(): AccentPreset? =
    runCatching { AccentPreset.valueOf(this) }.getOrNull()

private fun String.toCurrency(): Currency? =
    runCatching { Currency.valueOf(this) }.getOrNull()

private fun String.toThemeMode(): ThemeMode? =
    runCatching { ThemeMode.valueOf(this) }.getOrNull()
