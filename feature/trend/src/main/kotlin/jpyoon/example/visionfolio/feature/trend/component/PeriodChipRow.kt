package jpyoon.example.visionfolio.feature.trend.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.designsystem.component.VfChip
import jpyoon.example.visionfolio.domain.model.Period

private val OrderedPeriods = listOf(
    Period.D1 to R.string.period_chip_d1,
    Period.W1 to R.string.period_chip_w1,
    Period.M1 to R.string.period_chip_m1,
    Period.M3 to R.string.period_chip_m3,
    Period.M6 to R.string.period_chip_m6,
    Period.Y1 to R.string.period_chip_y1,
    Period.ALL to R.string.chip_all,
    Period.CUSTOM to R.string.period_custom,
)

@Composable
fun PeriodChipRow(
    selected: Period,
    available: List<Period>,
    onSelect: (Period) -> Unit,
    onOpenCustom: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OrderedPeriods
            .filter { (period, _) -> period in available }
            .forEach { (period, labelRes) ->
                VfChip(
                    text = stringResource(labelRes),
                    selected = period == selected,
                    onClick = {
                        if (period == Period.CUSTOM) onOpenCustom() else onSelect(period)
                    },
                )
            }
    }
}
