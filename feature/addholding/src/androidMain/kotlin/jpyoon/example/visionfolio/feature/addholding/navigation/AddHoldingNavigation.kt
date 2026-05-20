package jpyoon.example.visionfolio.feature.addholding.navigation

import androidx.compose.runtime.Composable
import jpyoon.example.visionfolio.feature.addholding.AddHoldingRoute

/**
 * AddHolding은 BottomSheet로 노출되므로 호출 지점에서 직접 사용합니다.
 */
@Composable
fun AddHoldingSheetScreen(
    onDismiss: () -> Unit,
    onCommitted: (String) -> Unit,
) {
    AddHoldingRoute(onDismiss = onDismiss, onCommitted = onCommitted)
}
