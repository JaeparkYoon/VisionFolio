package jpyoon.example.visionfolio.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopDestination(
    val route: String,
    val icon: ImageVector,
    val label: String,
) {
    HOME(NavRoutes.HOME, Icons.Outlined.Home, "홈"),
    DIVIDEND(NavRoutes.DIVIDEND, Icons.Outlined.PieChart, "배당"),
    CHAT(NavRoutes.CHAT, Icons.Outlined.Chat, "AI"),
    SETTINGS(NavRoutes.SETTINGS, Icons.Outlined.Settings, "설정"),
}
