package jpyoon.example.visionfolio.feature.dividend.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jpyoon.example.visionfolio.feature.dividend.DividendRoute
import jpyoon.example.visionfolio.navigation.NavRoutes

fun NavGraphBuilder.dividendScreen(
    onOpenGuru: (String) -> Unit,
) {
    composable(NavRoutes.DIVIDEND) {
        DividendRoute(onOpenGuru = onOpenGuru)
    }
}
