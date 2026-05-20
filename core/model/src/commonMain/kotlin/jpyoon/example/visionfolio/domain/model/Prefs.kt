package jpyoon.example.visionfolio.domain.model

enum class ThemeMode { SYSTEM, LIGHT, DARK }

enum class AccentPreset { SALMON, TERRACOTTA, CORAL, BURGUNDY }

enum class NotificationKey { DAILY_SUMMARY, HEADLINE_NEWS, PRICE_ALERT }

data class NotificationPrefs(
    val dailySummary: Boolean = true,
    val headlineNews: Boolean = true,
    val priceAlert: Boolean = false,
)

data class AppPrefs(
    val accentPreset: AccentPreset = AccentPreset.SALMON,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val hideAmounts: Boolean = false,
    val displayCurrency: Currency = Currency.KRW,
    val lastSyncAt: Long,
    val version: String = "1.0.0",
)

data class UserProfile(
    val displayName: String = "",
    val initial: String = "",
)

data class ErrorState(
    val message: String,
    val retryable: Boolean = true,
)
