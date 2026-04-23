package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.DividendSummary
import jpyoon.example.visionfolio.domain.model.formatter.CurrencyFormatter
import jpyoon.example.visionfolio.designsystem.R

@Composable
fun DividendSummaryCard(
    summary: DividendSummary?,
    displayCurrency: Currency,
    usdKrw: Double,
    modifier: Modifier = Modifier,
) {
    VfCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SummaryColumn(
                label = stringResource(R.string.dividend_yearly),
                amount = summary?.totalYearlyKrw ?: 0L,
                displayCurrency = displayCurrency,
                usdKrw = usdKrw,
            )
            SummaryColumn(
                label = stringResource(R.string.dividend_quarterly),
                amount = summary?.totalQuarterlyKrw ?: 0L,
                displayCurrency = displayCurrency,
                usdKrw = usdKrw,
            )
            SummaryColumn(
                label = stringResource(R.string.dividend_monthly),
                amount = summary?.totalMonthlyKrw ?: 0L,
                displayCurrency = displayCurrency,
                usdKrw = usdKrw,
            )
        }
    }
}

@Composable
private fun SummaryColumn(
    label: String,
    amount: Long,
    displayCurrency: Currency,
    usdKrw: Double,
) {
    val displayAmount = when (displayCurrency) {
        Currency.KRW -> amount
        Currency.USD -> (amount / usdKrw).toLong()
    }
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, style = VfTypography.LabelCaps, color = VfColors.InkTertiary)
        Text(
            text = CurrencyFormatter.format(displayAmount, displayCurrency, compact = true),
            style = VfTypography.BodyDefault,
            color = VfColors.InkPrimary,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
