package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfShapes
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import kotlinx.coroutines.delay

class VfToastState {
    var message by mutableStateOf<String?>(null)
        internal set

    fun show(text: String) {
        message = text
    }

    internal fun dismiss() {
        message = null
    }
}

@Composable
fun rememberVfToastState(): VfToastState = remember { VfToastState() }

@Composable
fun VfToastHost(
    state: VfToastState,
    modifier: Modifier = Modifier,
    durationMs: Long = 2200L,
) {
    LaunchedEffect(state.message) {
        if (state.message != null) {
            delay(durationMs)
            state.dismiss()
        }
    }

    Box(
        modifier = modifier.fillMaxWidth().padding(bottom = 24.dp),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedVisibility(
            visible = state.message != null,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
        ) {
            Surface(
                shape = VfShapes.Pill,
                color = VfColors.InkPrimary,
                shadowElevation = 8.dp,
            ) {
                Text(
                    text = state.message.orEmpty(),
                    style = VfTypography.BodyItem,
                    color = VfColors.BgDefault,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
                )
            }
        }
    }
}
