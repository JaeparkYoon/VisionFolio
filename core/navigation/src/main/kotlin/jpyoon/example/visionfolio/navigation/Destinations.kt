package jpyoon.example.visionfolio.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector


enum class TopDestination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    HOME("home", "홈", Icons.Outlined.Home),
    DIVIDEND("dividend", "인사이트", Icons.Outlined.Insights),
    SETTINGS("settings", "설정", Icons.Outlined.Settings),
}

object NavRoutes {
    const val HOME = "home"
    const val TREND = "trend"
    const val DIVIDEND = "dividend"
    const val SETTINGS = "settings"
    const val UPLOAD = "upload"
    const val HOLDINGS = "holdings"
    const val EDIT_HOLDINGS = "edit_holdings"
    const val LICENSES = "licenses"
    const val GURU_DETAIL = "guru_detail/{guruId}"
}
