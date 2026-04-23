package jpyoon.example.visionfolio.designsystem.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import jpyoon.example.visionfolio.domain.model.AccentPreset

val LocalAccent = staticCompositionLocalOf { VfAccentPalette.of(AccentPreset.SALMON) }

private fun materialScheme(scheme: VfColorScheme, accent: AccentColors, dark: Boolean) =
    if (dark) {
        darkColorScheme(
            primary = accent.base,
            onPrimary = scheme.bgDefault,
            primaryContainer = accent.wash,
            onPrimaryContainer = accent.ink,
            secondary = scheme.inkSecondary,
            onSecondary = scheme.bgDefault,
            tertiary = accent.ink,
            background = scheme.bgDefault,
            onBackground = scheme.inkPrimary,
            surface = scheme.card,
            onSurface = scheme.inkPrimary,
            surfaceVariant = scheme.bgAlt,
            onSurfaceVariant = scheme.inkSecondary,
            outline = scheme.lineDefault,
            outlineVariant = scheme.lineSoft,
        )
    } else {
        lightColorScheme(
            primary = accent.base,
            onPrimary = scheme.bgDefault,
            primaryContainer = accent.wash,
            onPrimaryContainer = accent.ink,
            secondary = scheme.inkSecondary,
            onSecondary = scheme.bgDefault,
            tertiary = accent.ink,
            background = scheme.bgDefault,
            onBackground = scheme.inkPrimary,
            surface = scheme.card,
            onSurface = scheme.inkPrimary,
            surfaceVariant = scheme.bgAlt,
            onSurfaceVariant = scheme.inkSecondary,
            outline = scheme.lineDefault,
            outlineVariant = scheme.lineSoft,
        )
    }

@Composable
fun VisionFolioTheme(
    accentPreset: AccentPreset = AccentPreset.SALMON,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val accent = VfAccentPalette.of(accentPreset, dark = darkTheme)
    val scheme = if (darkTheme) DarkVfColorScheme else LightVfColorScheme

    CompositionLocalProvider(
        LocalAccent provides accent,
        LocalVfColors provides scheme,
    ) {
        MaterialTheme(
            colorScheme = materialScheme(scheme, accent, darkTheme),
            typography = VfMaterialTypography,
            shapes = VfMaterialShapes,
            content = content,
        )
    }
}
