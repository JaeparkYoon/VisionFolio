package jpyoon.example.visionfolio.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.AssetBadge
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.VfChip
import jpyoon.example.visionfolio.domain.model.formatter.CurrencyFormatter
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.EnrichedHolding
import jpyoon.example.visionfolio.domain.model.displayName
import jpyoon.example.visionfolio.domain.model.isBond
import jpyoon.example.visionfolio.domain.model.isCash
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.AssetCategory.CASH
import jpyoon.example.visionfolio.domain.model.AssetCategory.PENSION

private const val PreviewRowCount = 6

@Composable
fun HoldingsCard(
    holdings: List<EnrichedHolding>,
    visibleCategory: AssetCategory?,
    hideAmounts: Boolean,
    displayCurrency: Currency,
    onFilter: (AssetCategory?) -> Unit,
    onOpenAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val categoriesInUse = remember(holdings) {
        holdings.map { it.holding.category }.distinct()
    }
    val filtered = remember(holdings, visibleCategory) {
        if (visibleCategory == null) holdings
        else holdings.filter { it.holding.category == visibleCategory }
    }

    VfCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(stringResource(R.string.label_holdings), style = VfTypography.HeadingSection, color = VfColors.InkPrimary)
            Text(
                text = stringResource(R.string.holdings_summary, holdings.size, categoriesInUse.size),
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            VfChip(stringResource(R.string.chip_all), selected = visibleCategory == null, onClick = { onFilter(null) })
            categoriesInUse.forEach { cat ->
                VfChip(
                    text = cat.displayName,
                    selected = visibleCategory == cat,
                    onClick = { onFilter(cat) },
                )
            }
        }

        Column(modifier = Modifier.padding(top = 14.dp)) {
            filtered.take(PreviewRowCount).forEachIndexed { index, enriched ->
                if (index > 0) HorizontalDivider(color = VfColors.LineSoft)
                HoldingRow(enriched = enriched, hideAmounts = hideAmounts, displayCurrency = displayCurrency)
            }
        }

        if (holdings.size > PreviewRowCount) {
            Text(
                text = stringResource(R.string.label_view_all),
                style = VfTypography.BodyItem.copy(fontWeight = FontWeight.W600),
                color = VfColors.InkSecondary,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .clickable(onClick = onOpenAll),
            )
        }
    }
}

@Composable
private fun HoldingRow(
    enriched: EnrichedHolding,
    hideAmounts: Boolean,
    displayCurrency: Currency,
) {
    val isCash = enriched.holding.category.isCash
    val isBond = enriched.holding.category.isBond
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AssetBadge(
            category = enriched.holding.category,
            initial = enriched.holding.name.take(1),
            imageUrl = enriched.holding.code.toLogoUrl(enriched.holding.category),
        )
        Column(
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
        ) {
            Text(
                enriched.holding.name,
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
                maxLines = 1,
            )
            Text(
                text = when {
                    isCash -> enriched.holding.category.displayName
                    isBond -> {
                        val maturity = enriched.holding.maturityDate
                        if (maturity.isNullOrBlank()) enriched.holding.category.displayName
                        else "${enriched.holding.category.displayName} · $maturity"
                    }
                    else -> stringResource(R.string.holding_detail_format, enriched.holding.category.displayName, formatQuantity(enriched.holding.quantity, stringResource(R.string.unit_shares)))
                },
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
                maxLines = 1,
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (hideAmounts) "••••" else CurrencyFormatter.format(enriched.valueKrw, displayCurrency, compact = true),
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
            )
            if (!isCash) {
                Text(
                    text = if (hideAmounts) "••" else enriched.holding.category.displayName,
                    style = VfTypography.MetaSub,
                    color = VfColors.InkTertiary,
                )
            }
        }
    }
}

private fun formatQuantity(q: Double, suffix: String): String =
    if (q % 1.0 == 0.0) "${q.toLong()}$suffix" else "%.4f".format(q)

private fun String.toLogoUrl(category: AssetCategory): String? =
    if (isBlank() || category == CASH || category == PENSION) null
    else "https://img.logokit.com/ticker/$this"
