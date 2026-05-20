package jpyoon.example.visionfolio.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.domain.model.NotificationKey
import jpyoon.example.visionfolio.feature.settings.component.GroupDivider
import jpyoon.example.visionfolio.feature.settings.component.ProfileCard
import jpyoon.example.visionfolio.feature.settings.component.RowTrailing
import jpyoon.example.visionfolio.feature.settings.component.SettingsGroup
import jpyoon.example.visionfolio.feature.settings.component.SettingsRow
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Composable
fun SettingsScreen(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    onOpenImport: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VfColors.BgDefault)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            stringResource(R.string.title_settings),
            style = VfTypography.TitleScreen,
            color = VfColors.InkPrimary,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        )

        ProfileCard(
            profile = state.profile,
            holdingCount = state.holdingCount,
            lastSyncAt = state.lastSyncAt,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
        )

        Spacer(Modifier.height(12.dp))

        SettingsGroup(title = stringResource(R.string.group_portfolio)) {
            SettingsRow(
                icon = Icons.Outlined.Add,
                headline = stringResource(R.string.settings_edit_assets),
                trailing = RowTrailing.Text(stringResource(R.string.holdings_count_format, state.holdingCount)),
                onClick = { onIntent(SettingsIntent.OpenEditAssets) },
            )
            GroupDivider()
            SettingsRow(
                icon = Icons.Outlined.Edit,
                headline = stringResource(R.string.settings_manage_holdings),
                trailing = RowTrailing.Chevron,
                onClick = { onIntent(SettingsIntent.OpenManageHoldings) },
            )
            GroupDivider()
            SettingsRow(
                icon = Icons.Outlined.Upload,
                headline = stringResource(R.string.settings_upload_screenshot),
                trailing = RowTrailing.Chevron,
                onClick = { onIntent(SettingsIntent.OpenUpload) },
            )
        }

        Spacer(Modifier.height(12.dp))

        SettingsGroup(title = stringResource(R.string.group_backup)) {
            SettingsRow(
                icon = Icons.Outlined.CloudDownload,
                headline = stringResource(R.string.settings_export_csv),
                trailing = RowTrailing.Chevron,
                onClick = { onIntent(SettingsIntent.RequestExport) },
            )
            GroupDivider()
            SettingsRow(
                icon = Icons.Outlined.CloudUpload,
                headline = stringResource(R.string.settings_import_csv),
                trailing = RowTrailing.Chevron,
                onClick = onOpenImport,
            )
        }

        Spacer(Modifier.height(12.dp))

        SettingsGroup(title = stringResource(R.string.group_notifications)) {
            SettingsRow(
                icon = Icons.Outlined.NotificationsNone,
                headline = stringResource(R.string.notif_daily_summary_name),
                trailing = RowTrailing.Toggle(
                    checked = state.notifications.dailySummary,
                    onChange = { onIntent(SettingsIntent.ToggleNotification(NotificationKey.DAILY_SUMMARY)) },
                ),
            )
            GroupDivider()
            SettingsRow(
                icon = Icons.Outlined.NotificationsNone,
                headline = stringResource(R.string.notif_headline_name),
                trailing = RowTrailing.Toggle(
                    checked = state.notifications.headlineNews,
                    onChange = { onIntent(SettingsIntent.ToggleNotification(NotificationKey.HEADLINE_NEWS)) },
                ),
            )
            GroupDivider()
            SettingsRow(
                icon = Icons.Outlined.NotificationsNone,
                headline = stringResource(R.string.notif_price_alert_name),
                trailing = RowTrailing.Toggle(
                    checked = state.notifications.priceAlert,
                    onChange = { onIntent(SettingsIntent.ToggleNotification(NotificationKey.PRICE_ALERT)) },
                ),
            )
        }

        Spacer(Modifier.height(12.dp))

        SettingsGroup(title = stringResource(R.string.group_app_info)) {
            SettingsRow(
                icon = Icons.Outlined.Info,
                headline = stringResource(R.string.settings_version),
                trailing = RowTrailing.Text(stringResource(R.string.settings_build_format, state.appVersion)),
            )
            GroupDivider()
            SettingsRow(
                icon = Icons.Outlined.Shield,
                headline = stringResource(R.string.settings_licenses),
                trailing = RowTrailing.Chevron,
                onClick = { onIntent(SettingsIntent.OpenLicenses) },
            )
            GroupDivider()
            SettingsRow(
                icon = Icons.Outlined.Article,
                headline = stringResource(R.string.settings_terms),
                trailing = RowTrailing.Chevron,
                onClick = { onIntent(SettingsIntent.OpenTerms) },
            )
        }

        Spacer(Modifier.height(36.dp))
        WordmarkFooter(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(96.dp))
    }
}

@Composable
private fun WordmarkFooter(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            stringResource(R.string.app_wordmark),
            style = VfTypography.HeadingSection,
            color = VfColors.InkTertiary,
        )
        Text(
            stringResource(R.string.app_tagline),
            style = VfTypography.MetaSub,
            color = VfColors.InkMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
