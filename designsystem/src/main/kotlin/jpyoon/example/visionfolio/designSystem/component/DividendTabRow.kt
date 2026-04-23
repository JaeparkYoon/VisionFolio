package jpyoon.example.visionfolio.designsystem.component

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

@Composable
fun DividendTabRow(
    selected: DividendTab,
    onSelect: (DividendTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        DividendTab.YEARLY to R.string.dividend_tab_yearly,
        DividendTab.QUARTERLY to R.string.dividend_tab_quarterly,
        DividendTab.MONTHLY to R.string.dividend_tab_monthly,
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tabs.forEach { (tab, labelRes) ->
            VfChip(
                text = stringResource(labelRes),
                selected = tab == selected,
                onClick = { onSelect(tab) },
            )
        }
    }
}
