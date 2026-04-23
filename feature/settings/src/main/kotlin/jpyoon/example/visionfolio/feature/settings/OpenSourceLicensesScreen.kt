package jpyoon.example.visionfolio.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Immutable
data class LicenseItem(
    val name: String,
    val author: String,
    val license: String,
)

private val licenses = listOf(
    LicenseItem("Kotlin", "JetBrains", "Apache License 2.0"),
    LicenseItem("Kotlin Coroutines", "JetBrains", "Apache License 2.0"),
    LicenseItem("Kotlin Serialization", "JetBrains", "Apache License 2.0"),
    LicenseItem("Jetpack Compose", "Google", "Apache License 2.0"),
    LicenseItem("Compose Material 3", "Google", "Apache License 2.0"),
    LicenseItem("Compose Navigation", "Google", "Apache License 2.0"),
    LicenseItem("AndroidX Core KTX", "Google", "Apache License 2.0"),
    LicenseItem("AndroidX Activity Compose", "Google", "Apache License 2.0"),
    LicenseItem("AndroidX Lifecycle", "Google", "Apache License 2.0"),
    LicenseItem("AndroidX DataStore", "Google", "Apache License 2.0"),
    LicenseItem("AndroidX Room", "Google", "Apache License 2.0"),
    LicenseItem("AndroidX WorkManager", "Google", "Apache License 2.0"),
    LicenseItem("Hilt (Dagger)", "Google", "Apache License 2.0"),
    LicenseItem("OkHttp", "Square", "Apache License 2.0"),
    LicenseItem("Coil", "Coil Contributors", "Apache License 2.0"),
)

@Composable
fun OpenSourceLicensesScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VfColors.BgDefault),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBackIosNew,
                contentDescription = stringResource(R.string.btn_close),
                tint = VfColors.InkPrimary,
                modifier = Modifier
                    .clickable(onClick = onBack)
                    .padding(16.dp)
                    .size(20.dp),
            )
            Text(
                text = stringResource(R.string.settings_licenses),
                style = VfTypography.TitleScreen,
                color = VfColors.InkPrimary,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            licenses.forEachIndexed { index, item ->
                LicenseRow(item)
                if (index < licenses.lastIndex) {
                    HorizontalDivider(
                        color = VfColors.LineSoft,
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            Text(
                text = stringResource(R.string.licenses_apache_notice),
                style = VfTypography.MetaSub,
                color = VfColors.InkMuted,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Spacer(Modifier.height(96.dp))
        }
    }
}

@Composable
private fun LicenseRow(item: LicenseItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Text(
            text = item.name,
            style = VfTypography.BodyDefault,
            color = VfColors.InkPrimary,
        )
        Spacer(Modifier.height(2.dp))
        Row {
            Text(
                text = item.author,
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = item.license,
                style = VfTypography.MetaSub,
                color = VfColors.InkMuted,
            )
        }
    }
}
