package jpyoon.example.visionfolio.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.content.ContextCompat
import jpyoon.example.visionfolio.app.di.AppViewModelFactory
import jpyoon.example.visionfolio.app.navigation.AppNavHost
import jpyoon.example.visionfolio.designsystem.di.LocalBillingRepository
import jpyoon.example.visionfolio.designsystem.di.LocalViewModelFactory
import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.designsystem.foundation.VisionFolioTheme

class MainActivity : ComponentActivity() {

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        maybeRequestNotificationPermission()

        val component = (application as VisionFolioApp).appComponent
        val viewModelFactory = AppViewModelFactory(component)

        setContent {
            CompositionLocalProvider(
                LocalViewModelFactory provides viewModelFactory,
                LocalBillingRepository provides component.billingRepository,
            ) {
                VisionFolioTheme(accentPreset = AccentPreset.CORAL) {
                    AppNavHost()
                }
            }
        }
    }

    private fun maybeRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
