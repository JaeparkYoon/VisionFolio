package jpyoon.example.visionfolio.feature.trend.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jpyoon.example.visionfolio.feature.trend.TrendRoute
import jpyoon.example.visionfolio.navigation.NavRoutes

fun NavGraphBuilder.trendScreen() {
    composable(NavRoutes.TREND) {
        TrendRoute()
    }
}
