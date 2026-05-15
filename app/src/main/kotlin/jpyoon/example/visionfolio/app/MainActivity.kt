package jpyoon.example.visionfolio.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import jpyoon.example.visionfolio.app.navigation.AppNavHost
import jpyoon.example.visionfolio.data.repository.AppPrefsRepository
import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.domain.model.ThemeMode
import jpyoon.example.visionfolio.designsystem.foundation.VisionFolioTheme
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var appPrefsRepo: AppPrefsRepository

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* no-op */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        maybeRequestNotificationPermission()

        setContent {
            val themeMode by appPrefsRepo.observePrefs()
                .map { it.themeMode }
                .collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
            val accentPreset by appPrefsRepo.observePrefs()
                .map { it.accentPreset }
                .collectAsStateWithLifecycle(initialValue = AccentPreset.CORAL)

            VisionFolioTheme(accentPreset = accentPreset, themeMode = themeMode) {
                AppNavHost()
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
