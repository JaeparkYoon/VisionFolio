package jpyoon.example.visionfolio.feature.dividend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.designsystem.component.GuruCardStrip
import jpyoon.example.visionfolio.designsystem.component.IndexCardStrip
import jpyoon.example.visionfolio.designsystem.component.NewsSection
import jpyoon.example.visionfolio.designsystem.component.SectionHeader
import jpyoon.example.visionfolio.designsystem.foundation.LocalAccent
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.designsystem.R
import jpyoon.example.visionfolio.feature.dividend.event.DividendEvents

@Composable
fun DividendScreen(
    state: DividendState,
    onEvent: (Events) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VfColors.BgDefault)
            .verticalScroll(rememberScrollState()),
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(
                stringResource(R.string.title_insight),
                style = VfTypography.TitleScreen,
                color = VfColors.InkPrimary,
            )
            Text(
                stringResource(R.string.subtitle_insight),
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    color = LocalAccent.current.base,
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 3.dp,
                )
            }
        } else {
            SectionHeader(title = stringResource(R.string.section_market_today))
            IndexCardStrip(
                indices = state.indices,
                onSelect = { onEvent(DividendEvents.ClickedOpenIndex(it)) },
                modifier = Modifier.padding(vertical = 8.dp),
            )

            Spacer(Modifier.height(16.dp))

            SectionHeader(
                title = stringResource(R.string.section_headline),
                subtitle = stringResource(R.string.section_headline_subtitle),
            )
            NewsSection(
                items = state.news,
                onClick = { onEvent(DividendEvents.ClickedOpenNews(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            Spacer(Modifier.height(16.dp))

            SectionHeader(
                title = stringResource(R.string.section_wall_street_gurus),
                subtitle = stringResource(R.string.section_wall_street_gurus_subtitle),
            )
            GuruCardStrip(
                gurus = state.gurus,
                onSelect = { onEvent(DividendEvents.ClickedOpenGuru(it)) },
                modifier = Modifier.padding(vertical = 8.dp),
            )

        }

        Spacer(Modifier.height(80.dp))
    }
}
