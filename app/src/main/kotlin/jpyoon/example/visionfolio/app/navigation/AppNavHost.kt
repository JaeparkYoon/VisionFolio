package jpyoon.example.visionfolio.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jpyoon.example.visionfolio.designsystem.component.VfToastHost
import jpyoon.example.visionfolio.designsystem.component.rememberVfToastState
import androidx.compose.ui.graphics.Color
import jpyoon.example.visionfolio.feature.addholding.editlist.editHoldingsScreen
import jpyoon.example.visionfolio.feature.addholding.navigation.AddHoldingSheetScreen
import jpyoon.example.visionfolio.feature.home.navigation.homeScreen
import jpyoon.example.visionfolio.feature.settings.navigation.licensesScreen
import jpyoon.example.visionfolio.feature.settings.navigation.settingsScreen
import jpyoon.example.visionfolio.feature.dividend.guru.guruDetailScreen
import jpyoon.example.visionfolio.feature.dividend.navigation.dividendScreen
import jpyoon.example.visionfolio.feature.trend.navigation.trendScreen
import jpyoon.example.visionfolio.feature.upload.navigation.uploadScreen
import jpyoon.example.visionfolio.navigation.NavRoutes
import jpyoon.example.visionfolio.navigation.TopDestination
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    var showAddHoldingSheet by rememberSaveable { mutableStateOf(false) }
    val addHoldingSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val toastState = rememberVfToastState()

    val openUpload: () -> Unit = remember(navController) {
        {
            navController.navigate(NavRoutes.UPLOAD) {
                launchSingleTop = true
            }
        }
    }
    val openAddHolding = remember { { showAddHoldingSheet = true } }
    val dismissAddHolding: () -> Unit = {
        scope.launch {
            addHoldingSheetState.hide()
            showAddHoldingSheet = false
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            NavHost(
                navController = navController,
                startDestination = NavRoutes.HOME,
            ) {
                homeScreen(
                    onOpenUpload = openUpload,
                    onOpenHoldings = {
                        navController.navigate(NavRoutes.EDIT_HOLDINGS) {
                            launchSingleTop = true
                        }
                    },
                    onOpenTrend = {
                        navController.navigate(NavRoutes.TREND) {
                            launchSingleTop = true
                        }
                    },
                )
                trendScreen()
                dividendScreen(
                    onOpenGuru = { guruId ->
                        navController.navigate("guru_detail/$guruId") {
                            launchSingleTop = true
                        }
                    },
                )
                guruDetailScreen(
                    onBack = { navController.popBackStack() },
                )
                settingsScreen(
                    onOpenUpload = openUpload,
                    onOpenAddHolding = openAddHolding,
                    onOpenManageHoldings = {
                        navController.navigate(NavRoutes.EDIT_HOLDINGS) {
                            launchSingleTop = true
                        }
                    },
                    onOpenLicenses = {
                        navController.navigate(NavRoutes.LICENSES) {
                            launchSingleTop = true
                        }
                    },
                    toastState = toastState,
                )
                licensesScreen(
                    onBack = { navController.popBackStack() },
                )
                editHoldingsScreen(
                    onDismiss = { navController.popBackStack() },
                    toastState = toastState,
                )
                uploadScreen(
                    onDismiss = { navController.popBackStack() },
                    onCommit = { added ->
                        toastState.show("${added}건이 추가되었어요")
                    },
                )
            }

            val showBottomBar = TopDestination.entries.any { it.route == currentRoute }
            if (showBottomBar) {
                BottomBar(
                    currentRoute = currentRoute,
                    onSelect = { dest ->
                        if (currentRoute == dest.route) return@BottomBar
                        navController.navigate(dest.route) {
                            popUpTo(NavRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                )
            }

            VfToastHost(state = toastState)
        }
    }

    if (showAddHoldingSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddHoldingSheet = false },
            sheetState = addHoldingSheetState,
            containerColor = Color.White,
        ) {
            AddHoldingSheetScreen(
                onDismiss = dismissAddHolding,
                onCommitted = { name -> toastState.show("${name}을(를) 추가했어요") },
            )
        }
    }
}
