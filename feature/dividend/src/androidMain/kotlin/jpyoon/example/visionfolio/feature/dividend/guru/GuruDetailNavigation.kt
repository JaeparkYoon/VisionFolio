package jpyoon.example.visionfolio.feature.dividend.guru

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import jpyoon.example.visionfolio.navigation.NavRoutes

fun NavGraphBuilder.guruDetailScreen(
    onBack: () -> Unit,
) {
    composable(
        route = NavRoutes.GURU_DETAIL,
        arguments = listOf(navArgument("guruId") { type = NavType.StringType }),
    ) { backStackEntry ->
        val guruId = backStackEntry.arguments?.getString("guruId") ?: return@composable
        GuruDetailRoute(guruId = guruId, onBack = onBack)
    }
}
