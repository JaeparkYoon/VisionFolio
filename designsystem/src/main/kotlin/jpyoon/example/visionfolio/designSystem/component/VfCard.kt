package jpyoon.example.visionfolio.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfShapes

@Composable
fun VfCard(
    modifier: Modifier = Modifier,
    shape: Shape = VfShapes.Lg,
    color: Color = VfColors.Card,
    border: BorderStroke? = BorderStroke(1.dp, VfColors.LineDefault),
    padding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
    elevation: Dp = 14.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shadowTint = VfColors.InkPrimary.copy(alpha = 0.06f)
    Surface(
        modifier = modifier.shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = shadowTint,
            spotColor = shadowTint,
        ),
        shape = shape,
        color = color,
        border = border,
    ) {
        Column(modifier = Modifier.padding(padding), content = content)
    }
}
