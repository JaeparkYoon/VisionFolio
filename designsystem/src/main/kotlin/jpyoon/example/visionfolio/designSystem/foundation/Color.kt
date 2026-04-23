package jpyoon.example.visionfolio.designsystem.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.domain.model.AssetCategory

data class VfColorScheme(
    val bgDefault: Color,
    val bgAlt: Color,
    val card: Color,
    val cardAccent: Color,
    val inkPrimary: Color,
    val inkSecondary: Color,
    val inkTertiary: Color,
    val inkMuted: Color,
    val lineDefault: Color,
    val lineSoft: Color,
    val up: Color,
    val down: Color,
    val neutral: Color,
)

val LightVfColorScheme = VfColorScheme(
    bgDefault = Color(0xFFFFFFFF),
    bgAlt = Color(0xFFF5F2EE),
    card = Color(0xFFFFFFFF),
    cardAccent = Color(0xFFFBF9F6),
    inkPrimary = Color(0xFF1F1B17),
    inkSecondary = Color(0xFF5E544B),
    inkTertiary = Color(0xFF9A9086),
    inkMuted = Color(0xFFB8AFA3),
    lineDefault = Color(0xFFEBE6DE),
    lineSoft = Color(0xFFF3EFE8),
    up = Color(0xFFB55D4B),
    down = Color(0xFF4A6B7A),
    neutral = Color(0xFF8A7F72),
)

val DarkVfColorScheme = VfColorScheme(
    bgDefault = Color(0xFF0E0C0A),
    bgAlt = Color(0xFF1A1713),
    card = Color(0xFF14110E),
    cardAccent = Color(0xFF1A1713),
    inkPrimary = Color(0xFFF5EFE7),
    inkSecondary = Color(0xFFC8BDAD),
    inkTertiary = Color(0xFF9A9086),
    inkMuted = Color(0xFF5E544B),
    lineDefault = Color(0xFF2A2520),
    lineSoft = Color(0xFF1F1C18),
    up = Color(0xFFD98574),
    down = Color(0xFF7F9DAC),
    neutral = Color(0xFF8A7F72),
)

val LocalVfColors = staticCompositionLocalOf { LightVfColorScheme }

@Suppress("unused")
object VfColors {
    val BgDefault: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.bgDefault
    val BgAlt: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.bgAlt
    val Card: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.card
    val CardAccent: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.cardAccent
    val InkPrimary: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.inkPrimary
    val InkSecondary: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.inkSecondary
    val InkTertiary: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.inkTertiary
    val InkMuted: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.inkMuted
    val LineDefault: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.lineDefault
    val LineSoft: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.lineSoft
    val Up: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.up
    val Down: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.down
    val Neutral: Color
        @Composable @ReadOnlyComposable get() = LocalVfColors.current.neutral
}

data class AccentColors(
    val base: Color,
    val dim: Color,
    val wash: Color,
    val ink: Color,
)

object VfAccentPalette {

    private val SalmonLight = AccentColors(Color(0xFFD98574), Color(0xFFE8B5A9), Color(0xFFF5E8E4), Color(0xFFB55D4B))
    private val TerracottaLight = AccentColors(Color(0xFFC0604F), Color(0xFFD9927F), Color(0xFFEFDBD2), Color(0xFF984635))
    private val CoralLight = AccentColors(Color(0xFFE88A7A), Color(0xFFF2B8AD), Color(0xFFFCEAE4), Color(0xFFC06051))
    private val BurgundyLight = AccentColors(Color(0xFFA54A4A), Color(0xFFC8817F), Color(0xFFECD8D6), Color(0xFF7E3333))

    private val SalmonDark = AccentColors(Color(0xFFE29986), Color(0xFF7E4A3E), Color(0xFF3A211C), Color(0xFFF5B2A3))
    private val TerracottaDark = AccentColors(Color(0xFFD07260), Color(0xFF70382C), Color(0xFF351B15), Color(0xFFEFA491))
    private val CoralDark = AccentColors(Color(0xFFED9D8E), Color(0xFF7A4238), Color(0xFF3B201C), Color(0xFFF8C1B5))
    private val BurgundyDark = AccentColors(Color(0xFFC4716F), Color(0xFF5C2F2F), Color(0xFF2E1818), Color(0xFFE09797))

    fun of(preset: AccentPreset, dark: Boolean = false): AccentColors = when (preset) {
        AccentPreset.SALMON -> if (dark) SalmonDark else SalmonLight
        AccentPreset.TERRACOTTA -> if (dark) TerracottaDark else TerracottaLight
        AccentPreset.CORAL -> if (dark) CoralDark else CoralLight
        AccentPreset.BURGUNDY -> if (dark) BurgundyDark else BurgundyLight
    }
}

object VfCategoryColors {
    val DomesticStock = Color(0xFFD98574)
    val OverseasStock = Color(0xFFC49A6C)
    val Etf = Color(0xFF8FA58C)
    val Crypto = Color(0xFFB57A8A)
    val Bond = Color(0xFF7B8FA1)
    val Cash = Color(0xFFA89B85)
    val Pension = Color(0xFF8A7F72)

    fun of(category: AssetCategory): Color = when (category) {
        AssetCategory.DOMESTIC_STOCK -> DomesticStock
        AssetCategory.OVERSEAS_STOCK -> OverseasStock
        AssetCategory.ETF -> Etf
        AssetCategory.CRYPTO -> Crypto
        AssetCategory.BOND -> Bond
        AssetCategory.CASH -> Cash
        AssetCategory.PENSION -> Pension
    }
}
