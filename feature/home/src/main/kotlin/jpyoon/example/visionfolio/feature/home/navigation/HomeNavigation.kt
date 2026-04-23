package jpyoon.example.visionfolio.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jpyoon.example.visionfolio.feature.home.HomeRoute
import jpyoon.example.visionfolio.navigation.NavRoutes

fun NavGraphBuilder.homeScreen(
    onOpenUpload: () -> Unit,
    onOpenHoldings: () -> Unit,
    onOpenTrend: () -> Unit,
) {
    composable(NavRoutes.HOME) {
        HomeRoute(
            onOpenUpload = onOpenUpload,
            onOpenHoldings = onOpenHoldings,
            onOpenTrend = onOpenTrend,
        )
    }
}
