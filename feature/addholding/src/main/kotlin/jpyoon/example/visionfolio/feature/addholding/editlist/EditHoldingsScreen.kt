package jpyoon.example.visionfolio.feature.addholding.editlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.domain.model.displayName
import jpyoon.example.visionfolio.domain.model.isBond
import jpyoon.example.visionfolio.domain.model.isCash
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EditHoldingsScreen(
    state: EditHoldingsState,
    onIntent: (EditHoldingsIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VfColors.BgDefault),
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = null,
                    tint = VfColors.InkPrimary,
                )
            }
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text(
                    stringResource(R.string.title_edit_holdings),
                    style = VfTypography.TitleScreen,
                    color = VfColors.InkPrimary,
                )
                Text(
                    stringResource(R.string.subtitle_edit_holdings),
                    style = VfTypography.MetaSub,
                    color = VfColors.InkTertiary,
                )
            }
        }

        if (state.holdings.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    stringResource(R.string.empty_holdings),
                    style = VfTypography.HeadingSection,
                    color = VfColors.InkMuted,
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.holdings, key = { it.id }) { holding ->
                    HoldingRow(
                        holding = holding,
                        onEdit = { onIntent(EditHoldingsIntent.StartEdit(holding)) },
                        onDelete = { onIntent(EditHoldingsIntent.Delete(holding.id)) },
                    )
                    HorizontalDivider(color = VfColors.BgAlt)
                }
            }
        }
    }
}

@Composable
private fun HoldingRow(
    holding: Holding,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val nf = NumberFormat.getNumberInstance(Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                holding.name,
                style = VfTypography.HeadingSection,
                color = VfColors.InkPrimary,
            )
            Spacer(Modifier.height(2.dp))
            val detail = when {
                holding.category.isCash -> {
                    "${holding.category.displayName} · ${holding.currency.name} ${nf.format(holding.quantity.toLong())}"
                }
                holding.category.isBond -> {
                    val maturity = holding.maturityDate
                    val base = "${holding.category.displayName} · ${holding.currency.name} ${nf.format(holding.avgPrice.toLong())}"
                    if (maturity.isNullOrBlank()) base else "$base · $maturity"
                }
                else -> {
                    "${holding.category.displayName} · ${nf.format(holding.quantity)} ${stringResource(R.string.unit_shares)} · ${holding.currency.name} ${nf.format(holding.currentPrice.toLong())}"
                }
            }
            Text(
                detail,
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(onClick = onEdit, modifier = Modifier.size(40.dp)) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.cd_edit),
                    tint = VfColors.InkSecondary,
                    modifier = Modifier.size(20.dp),
                )
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = stringResource(R.string.cd_remove),
                    tint = VfColors.Down,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}
