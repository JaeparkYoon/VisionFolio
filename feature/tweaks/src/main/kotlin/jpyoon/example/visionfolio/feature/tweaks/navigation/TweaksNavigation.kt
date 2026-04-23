package jpyoon.example.visionfolio.feature.tweaks.navigation

import androidx.compose.runtime.Composable
import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.feature.tweaks.TweaksRoute

/**
 * Tweaks는 오버레이로 노출되므로 호출 지점에서 직접 사용합니다.
 */
@Composable
fun TweaksOverlay(
    onClose: () -> Unit,
    onAccentChanged: (AccentPreset) -> Unit = {},
) {
    TweaksRoute(onClose = onClose, onAccentChanged = onAccentChanged)
}
