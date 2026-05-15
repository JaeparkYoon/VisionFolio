package jpyoon.example.visionfolio.feature.returns.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jpyoon.example.visionfolio.feature.returns.ReturnsRoute
import jpyoon.example.visionfolio.navigation.NavRoutes

fun NavGraphBuilder.returnsScreen(
    onBack: () -> Unit,
) {
    composable(NavRoutes.RETURNS) {
        ReturnsRoute(onBack = onBack)
    }
}
