package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.HoldingDividendInfo
import jpyoon.example.visionfolio.domain.model.PaymentFrequency
import jpyoon.example.visionfolio.domain.model.formatter.CurrencyFormatter
import jpyoon.example.visionfolio.designsystem.R
import java.text.DecimalFormat

@Composable
fun HoldingDividendList(
    holdings: List<HoldingDividendInfo>,
    selectedTab: DividendTab,
    displayCurrency: Currency,
    usdKrw: Double,
    modifier: Modifier = Modifier,
) {
    if (holdings.isEmpty()) {
        Text(
            text = stringResource(R.string.dividend_empty),
            style = VfTypography.BodyDefault,
            color = VfColors.InkTertiary,
            modifier = modifier.padding(horizontal = 20.dp, vertical = 24.dp),
        )
        return
    }

    VfCard(modifier = modifier) {
        holdings.forEachIndexed { index, info ->
            if (index > 0) {
                HorizontalDivider(
                    color = VfColors.LineSoft,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            }
            HoldingDividendRow(
                info = info,
                selectedTab = selectedTab,
                displayCurrency = displayCurrency,
                usdKrw = usdKrw,
            )
        }
    }
}

@Composable
private fun HoldingDividendRow(
    info: HoldingDividendInfo,
    selectedTab: DividendTab,
    displayCurrency: Currency,
    usdKrw: Double,
) {
    val divisor = when (selectedTab) {
        DividendTab.YEARLY -> 1
        DividendTab.QUARTERLY -> 4
        DividendTab.MONTHLY -> 12
    }
    val totalKrw = when (info.currency) {
        Currency.KRW -> (info.annualDividendTotal / divisor).toLong()
        Currency.USD -> ((info.annualDividendTotal * usdKrw) / divisor).toLong()
    }
    val displayAmount = when (displayCurrency) {
        Currency.KRW -> totalKrw
        Currency.USD -> (totalKrw / usdKrw).toLong()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = info.stockName,
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
            )
            Text(
                text = buildString {
                    if (info.stockCode.isNotBlank()) {
                        append(info.stockCode)
                        append(" · ")
                    }
                    append(frequencyLabel(info.frequency))
                    append(" · ")
                    append(yieldFormat.format(info.dividendYield))
                    append("%")
                },
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = CurrencyFormatter.format(displayAmount, displayCurrency),
                style = VfTypography.BodyDefault,
                color = VfColors.InkPrimary,
            )
            Text(
                text = stringResource(
                    R.string.dividend_per_share_format,
                    dpsFormat.format(info.dividendPerShare),
                    info.currency.name,
                ),
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

@Composable
private fun frequencyLabel(frequency: PaymentFrequency): String = when (frequency) {
    PaymentFrequency.MONTHLY -> stringResource(R.string.frequency_monthly)
    PaymentFrequency.QUARTERLY -> stringResource(R.string.frequency_quarterly)
    PaymentFrequency.SEMI_ANNUAL -> stringResource(R.string.frequency_semi_annual)
    PaymentFrequency.ANNUAL -> stringResource(R.string.frequency_annual)
    PaymentFrequency.UNKNOWN -> stringResource(R.string.frequency_unknown)
}

private val yieldFormat = DecimalFormat("0.##")
private val dpsFormat = DecimalFormat("#,##0.##")
