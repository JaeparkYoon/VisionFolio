package jpyoon.example.visionfolio.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jpyoon.example.visionfolio.designsystem.component.VfToastState
import jpyoon.example.visionfolio.feature.settings.OpenSourceLicensesScreen
import jpyoon.example.visionfolio.feature.settings.SettingsRoute
import jpyoon.example.visionfolio.navigation.NavRoutes

fun NavGraphBuilder.settingsScreen(
    onOpenUpload: () -> Unit,
    onOpenAddHolding: () -> Unit,
    onOpenManageHoldings: () -> Unit,
    onOpenLicenses: () -> Unit,
    onOpenReturns: () -> Unit,
    onOpenAnnouncements: () -> Unit,
    toastState: VfToastState,
) {
    composable(NavRoutes.SETTINGS) {
        SettingsRoute(
            onOpenUpload = onOpenUpload,
            onOpenAddHolding = onOpenAddHolding,
            onOpenManageHoldings = onOpenManageHoldings,
            onOpenLicenses = onOpenLicenses,
            onOpenReturns = onOpenReturns,
            onOpenAnnouncements = onOpenAnnouncements,
            toastState = toastState,
        )
    }
}

fun NavGraphBuilder.licensesScreen(
    onBack: () -> Unit,
) {
    composable(NavRoutes.LICENSES) {
        OpenSourceLicensesScreen(onBack = onBack)
    }
}
