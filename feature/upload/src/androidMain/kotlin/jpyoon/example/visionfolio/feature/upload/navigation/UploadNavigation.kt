package jpyoon.example.visionfolio.feature.upload.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jpyoon.example.visionfolio.feature.upload.UploadRoute
import jpyoon.example.visionfolio.navigation.NavRoutes

fun NavGraphBuilder.uploadScreen(
    onDismiss: () -> Unit,
    onCommit: (Int) -> Unit,
) {
    composable(NavRoutes.UPLOAD) {
        UploadRoute(onDismiss = onDismiss, onCommit = onCommit)
    }
}

/**
 * 기존 BottomSheet 호출 호환용. 향후 제거 예정.
 */
@Composable
fun UploadSheetScreen(
    onDismiss: () -> Unit,
    onCommit: (Int) -> Unit,
) {
    UploadRoute(onDismiss = onDismiss, onCommit = onCommit)
}
