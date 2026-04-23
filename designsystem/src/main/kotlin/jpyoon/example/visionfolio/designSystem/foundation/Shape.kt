package jpyoon.example.visionfolio.designsystem.foundation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object VfShapes {
    val Sm = RoundedCornerShape(10.dp)
    val Md = RoundedCornerShape(14.dp)
    val Lg = RoundedCornerShape(20.dp)
    val Xl = RoundedCornerShape(28.dp)
    val Pill = RoundedCornerShape(999.dp)
}

val VfMaterialShapes = Shapes(
    extraSmall = VfShapes.Sm,
    small = VfShapes.Md,
    medium = VfShapes.Lg,
    large = VfShapes.Xl,
    extraLarge = VfShapes.Xl,
)
