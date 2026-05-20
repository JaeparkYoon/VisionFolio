package jpyoon.example.visionfolio.shared

import jpyoon.example.visionfolio.core.common.di.AppSingleton
import jpyoon.example.visionfolio.core.repository.api.AppPrefsRepository
import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.domain.model.AppPrefs
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.NotificationKey
import jpyoon.example.visionfolio.domain.model.NotificationPrefs
import jpyoon.example.visionfolio.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import me.tatarka.inject.annotations.Inject
import platform.Foundation.NSUserDefaults
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * NSUserDefaults-based iOS AppPrefsRepository implementation.
 */
@AppSingleton
@Inject
class IosAppPrefsRepository : AppPrefsRepository {

    private val defaults = NSUserDefaults.standardUserDefaults

    private val prefsFlow = MutableStateFlow(readPrefs())
    private val notifFlow = MutableStateFlow(readNotifPrefs())
    private val denialFlow = MutableStateFlow(defaults.integerForKey(Keys.NOTIF_DENIAL_COUNT).toInt())
    private val uuidFlow = MutableStateFlow(readOrCreateUuid())
    private val guideHiddenFlow = MutableStateFlow(defaults.boolForKey(Keys.SCREENSHOT_GUIDE_HIDDEN))

    override fun observePrefs(): Flow<AppPrefs> = prefsFlow

    override fun observeNotifications(): Flow<NotificationPrefs> = notifFlow

    override suspend fun setAccent(preset: AccentPreset) {
        defaults.setObject(preset.name, forKey = Keys.ACCENT)
        prefsFlow.update { it.copy(accentPreset = preset) }
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        defaults.setObject(mode.name, forKey = Keys.THEME_MODE)
    }

    override suspend fun setHideAmounts(hide: Boolean) {
        defaults.setBool(hide, forKey = Keys.HIDE_AMOUNTS)
        prefsFlow.update { it.copy(hideAmounts = hide) }
    }

    override suspend fun setDisplayCurrency(currency: Currency) {
        defaults.setObject(currency.name, forKey = Keys.DISPLAY_CURRENCY)
        prefsFlow.update { it.copy(displayCurrency = currency) }
    }

    override suspend fun toggleNotification(key: NotificationKey) {
        notifFlow.update { prefs ->
            when (key) {
                NotificationKey.DAILY_SUMMARY -> {
                    val new = !prefs.dailySummary
                    defaults.setBool(new, forKey = Keys.NOTIF_DAILY)
                    prefs.copy(dailySummary = new)
                }
                NotificationKey.HEADLINE_NEWS -> {
                    val new = !prefs.headlineNews
                    defaults.setBool(new, forKey = Keys.NOTIF_HEADLINE)
                    prefs.copy(headlineNews = new)
                }
                NotificationKey.PRICE_ALERT -> {
                    val new = !prefs.priceAlert
                    defaults.setBool(new, forKey = Keys.NOTIF_PRICE)
                    prefs.copy(priceAlert = new)
                }
            }
        }
    }

    override fun observeNotifDenialCount(): Flow<Int> = denialFlow

    override suspend fun incrementNotifDenialCount() {
        val new = denialFlow.value + 1
        defaults.setInteger(new.toLong(), forKey = Keys.NOTIF_DENIAL_COUNT)
        denialFlow.value = new
    }

    override suspend fun resetNotifDenialCount() {
        defaults.setInteger(0, forKey = Keys.NOTIF_DENIAL_COUNT)
        denialFlow.value = 0
    }

    override fun observeUserUuid(): Flow<String> = uuidFlow

    override fun observeScreenshotGuideHidden(): Flow<Boolean> = guideHiddenFlow

    override suspend fun setScreenshotGuideHidden(hidden: Boolean) {
        defaults.setBool(hidden, forKey = Keys.SCREENSHOT_GUIDE_HIDDEN)
        guideHiddenFlow.value = hidden
    }

    private val baselineFlows = mutableMapOf<Int, MutableStateFlow<Long?>>()

    override fun observeReturnBaseline(year: Int): Flow<Long?> = baselineFlows.getOrPut(year) {
        val key = "return_baseline_$year"
        val stored = (defaults.objectForKey(key) as? platform.Foundation.NSNumber)?.longLongValue
        MutableStateFlow(stored)
    }

    override suspend fun setReturnBaseline(year: Int, krw: Long?) {
        val key = "return_baseline_$year"
        if (krw == null) {
            defaults.removeObjectForKey(key)
        } else {
            defaults.setInteger(krw, forKey = key)
        }
        baselineFlows.getOrPut(year) { MutableStateFlow(krw) }.value = krw
    }

    private fun readPrefs(): AppPrefs {
        val accentName = defaults.stringForKey(Keys.ACCENT)
        val accent = accentName?.let { runCatching { AccentPreset.valueOf(it) }.getOrNull() }
            ?: AccentPreset.SALMON
        val currencyName = defaults.stringForKey(Keys.DISPLAY_CURRENCY)
        val currency = currencyName?.let { runCatching { Currency.valueOf(it) }.getOrNull() }
            ?: Currency.KRW
        return AppPrefs(
            accentPreset = accent,
            hideAmounts = defaults.boolForKey(Keys.HIDE_AMOUNTS),
            displayCurrency = currency,
            lastSyncAt = defaults.integerForKey(Keys.LAST_SYNC_AT).let {
                if (it == 0L) kotlinx.datetime.Clock.System.now().toEpochMilliseconds() else it
            },
            version = defaults.stringForKey(Keys.VERSION) ?: "1.0.0",
        )
    }

    private fun readNotifPrefs(): NotificationPrefs {
        val daily = if (defaults.objectForKey(Keys.NOTIF_DAILY) != null)
            defaults.boolForKey(Keys.NOTIF_DAILY) else true
        val headline = if (defaults.objectForKey(Keys.NOTIF_HEADLINE) != null)
            defaults.boolForKey(Keys.NOTIF_HEADLINE) else true
        val price = defaults.boolForKey(Keys.NOTIF_PRICE)
        return NotificationPrefs(dailySummary = daily, headlineNews = headline, priceAlert = price)
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun readOrCreateUuid(): String {
        val existing = defaults.stringForKey(Keys.USER_UUID)
        if (!existing.isNullOrBlank()) return existing
        val uuid = Uuid.random().toString()
        defaults.setObject(uuid, forKey = Keys.USER_UUID)
        return uuid
    }

    private object Keys {
        const val ACCENT = "accent_preset"
        const val THEME_MODE = "theme_mode"
        const val HIDE_AMOUNTS = "hide_amounts"
        const val DISPLAY_CURRENCY = "display_currency"
        const val LAST_SYNC_AT = "last_sync_at"
        const val VERSION = "version"
        const val NOTIF_DAILY = "notif_daily"
        const val NOTIF_HEADLINE = "notif_headline"
        const val NOTIF_PRICE = "notif_price"
        const val NOTIF_DENIAL_COUNT = "notif_denial_count"
        const val USER_UUID = "user_uuid"
        const val SCREENSHOT_GUIDE_HIDDEN = "screenshot_guide_hidden"
    }
}
