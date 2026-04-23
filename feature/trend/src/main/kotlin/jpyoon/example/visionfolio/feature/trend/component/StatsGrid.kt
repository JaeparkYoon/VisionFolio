package jpyoon.example.visionfolio.feature.trend.component

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
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.domain.model.formatter.CurrencyFormatter
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.IntervalStats
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography

@Composable
fun StatsGrid(
    stats: IntervalStats?,
    displayCurrency: Currency = Currency.KRW,
    usdKrw: Double = 1388.0,
    modifier: Modifier = Modifier,
) {
    VfCard(modifier = modifier) {
        val items = listOf(
            R.string.stat_high to (stats?.max ?: 0L),
            R.string.stat_low to (stats?.min ?: 0L),
            R.string.stat_avg to (stats?.avg ?: 0L),
            R.string.stat_start to (stats?.start ?: 0L),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            items.forEach { (labelRes, value) ->
                val displayValue = when (displayCurrency) {
                    Currency.KRW -> value
                    Currency.USD -> (value / usdKrw).toLong()
                }
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(stringResource(labelRes), style = VfTypography.LabelCaps, color = VfColors.InkTertiary)
                    Text(
                        text = CurrencyFormatter.format(displayValue, displayCurrency, compact = true),
                        style = VfTypography.BodyDefault,
                        color = VfColors.InkPrimary,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }
}
