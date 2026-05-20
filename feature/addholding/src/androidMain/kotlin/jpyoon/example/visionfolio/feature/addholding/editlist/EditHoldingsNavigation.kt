package jpyoon.example.visionfolio.feature.addholding.editlist

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jpyoon.example.visionfolio.designsystem.component.VfToastState
import jpyoon.example.visionfolio.navigation.NavRoutes

fun NavGraphBuilder.editHoldingsScreen(
    onDismiss: () -> Unit,
    toastState: VfToastState,
) {
    composable(NavRoutes.EDIT_HOLDINGS) {
        EditHoldingsRoute(
            onDismiss = onDismiss,
            toastState = toastState,
        )
    }
}
