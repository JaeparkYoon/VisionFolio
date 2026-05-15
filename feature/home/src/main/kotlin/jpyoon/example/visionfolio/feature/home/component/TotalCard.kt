package jpyoon.example.visionfolio.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.domain.model.formatter.CurrencyFormatter
import jpyoon.example.visionfolio.domain.model.formatter.PercentFormatter
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.PortfolioSummary
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.foundation.VfShapes
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Composable
fun TotalCard(
    summary: PortfolioSummary?,
    hideAmounts: Boolean,
    displayCurrency: Currency,
    onToggleHide: () -> Unit,
    onToggleCurrency: () -> Unit,
    onOpenUpload: () -> Unit,
    onOpenTrend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = LocalAccent.current
    VfCard(
        modifier = modifier,
        shape = VfShapes.Xl,
        padding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 20.dp,
            vertical = 22.dp,
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(stringResource(R.string.label_total_assets), style = VfTypography.LabelCaps, color = VfColors.InkTertiary)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                CurrencyToggle(currency = displayCurrency, onClick = onToggleCurrency)
                EyeToggle(hidden = hideAmounts, onClick = onToggleHide)
            }
        }

        AmountRow(
            amount = summary?.totalValue ?: 0L,
            hidden = hideAmounts,
            displayCurrency = displayCurrency,
            modifier = Modifier.padding(top = 8.dp),
        )

        DeltaRow(
            label = stringResource(R.string.label_today),
            amount = summary?.dayChange ?: 0L,
            pct = summary?.dayPct ?: 0.0,
            hidden = hideAmounts,
            displayCurrency = displayCurrency,
            modifier = Modifier.padding(top = 14.dp),
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            TrendCtaStrip(onClick = onOpenTrend, modifier = Modifier.weight(1f))
            UploadCtaStrip(onClick = onOpenUpload, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun EyeToggle(hidden: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(VfShapes.Pill)
            .background(VfColors.BgAlt)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = if (hidden) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
            contentDescription = if (hidden) stringResource(R.string.cd_show_amount) else stringResource(R.string.cd_hide_amount),
            tint = VfColors.InkSecondary,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun CurrencyToggle(currency: Currency, onClick: () -> Unit) {
    val label = when (currency) {
        Currency.KRW -> "₩"
        Currency.USD -> "$"
    }
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(VfShapes.Pill)
            .background(VfColors.BgAlt)
            .clickable(
                onClick = onClick,
                onClickLabel = stringResource(R.string.cd_toggle_currency),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = VfTypography.BodyDefault.copy(fontWeight = FontWeight.W700),
            color = VfColors.InkSecondary,
        )
    }
}

@Composable
private fun AmountRow(
    amount: Long,
    hidden: Boolean,
    displayCurrency: Currency,
    modifier: Modifier = Modifier,
) {
    val symbol = when (displayCurrency) {
        Currency.KRW -> "₩"
        Currency.USD -> "$"
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
    ) {
        if (!hidden) {
            Text(
                text = symbol,
                style = VfTypography.DisplayTotal,
                color = VfColors.InkPrimary,
                maxLines = 1,
                modifier = Modifier.padding(end = 2.dp),
            )
        }
        Text(
            text = if (hidden) "•••••" else "%,d".format(amount),
            style = VfTypography.DisplayTotal,
            color = VfColors.InkPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false),
        )
    }
}

@Composable
private fun DeltaRow(
    label: String,
    amount: Long,
    pct: Double,
    hidden: Boolean,
    displayCurrency: Currency,
    modifier: Modifier = Modifier,
) {
    val up = amount >= 0
    val color = if (up) VfColors.Up else VfColors.Down
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = VfTypography.MetaSub,
            color = VfColors.InkTertiary,
            modifier = Modifier.padding(end = 10.dp),
        )
        if (hidden) {
            Text("••••", style = VfTypography.BodyItem, color = VfColors.InkMuted)
        } else {
            Text(
                text = (if (up) "▲ " else "▼ ") + CurrencyFormatter.format(amount, displayCurrency, compact = true),
                style = VfTypography.BodyItem,
                color = color,
            )
            Text(
                text = " (" + PercentFormatter.format(pct) + ")",
                style = VfTypography.MetaSub,
                color = color,
            )
        }
    }
}

@Composable
private fun TrendCtaStrip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = LocalAccent.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(accent.wash)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.ShowChart,
            contentDescription = null,
            tint = accent.ink,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = stringResource(R.string.cta_view_trend),
            style = VfTypography.MetaSub.copy(fontWeight = FontWeight.W600),
            color = accent.ink,
            modifier = Modifier.padding(start = 6.dp),
        )
    }
}

@Composable
private fun UploadCtaStrip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = LocalAccent.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(accent.wash)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = null,
            tint = accent.ink,
            modifier = Modifier.size(18.dp),
        )
        Text(
            text = stringResource(R.string.cta_upload_screenshot),
            style = VfTypography.MetaSub.copy(fontWeight = FontWeight.W600),
            color = accent.ink,
            modifier = Modifier.padding(start = 6.dp),
        )
    }
}
