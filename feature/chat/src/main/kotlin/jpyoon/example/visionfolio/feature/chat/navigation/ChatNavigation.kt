package jpyoon.example.visionfolio.feature.chat.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jpyoon.example.visionfolio.feature.chat.ChatRoute
import jpyoon.example.visionfolio.navigation.NavRoutes

fun NavGraphBuilder.chatScreen(
    onBack: () -> Unit,
) {
    composable(NavRoutes.CHAT) {
        ChatRoute(onBack = onBack)
    }
}
