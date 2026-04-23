package jpyoon.example.visionfolio.designsystem.foundation

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Pretendard = FontFamily.Default

object VfTypography {
    val DisplayTotal = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 34.sp,
        lineHeight = 37.4.sp,
        letterSpacing = (-0.85).sp,
    )
    val DisplayTrend = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W700,
        fontSize = 40.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.8).sp,
    )
    val TitleScreen = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 26.sp,
        lineHeight = 26.sp,
        letterSpacing = (-0.52).sp,
    )
    val TitleQuote = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 15.sp,
        lineHeight = 21.75.sp,
        letterSpacing = (-0.15).sp,
    )
    val HeadingSection = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 15.sp,
        lineHeight = 18.sp,
        letterSpacing = (-0.15).sp,
    )
    val BodyDefault = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        fontSize = 14.5.sp,
        lineHeight = 20.3.sp,
        letterSpacing = (-0.145).sp,
    )
    val BodyItem = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        letterSpacing = (-0.14).sp,
    )
    val LabelCaps = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W600,
        fontSize = 11.sp,
        lineHeight = 11.sp,
        letterSpacing = 0.88.sp,
    )
    val MetaSub = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        lineHeight = 15.6.sp,
    )
    val MetaSubSm = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.W500,
        fontSize = 11.5.sp,
        lineHeight = 14.95.sp,
    )
    val MonoHash = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W500,
        fontSize = 10.5.sp,
        lineHeight = 13.65.sp,
    )
}

val VfMaterialTypography = Typography(
    displayLarge = VfTypography.DisplayTrend,
    displayMedium = VfTypography.DisplayTotal,
    titleLarge = VfTypography.TitleScreen,
    titleMedium = VfTypography.TitleQuote,
    titleSmall = VfTypography.HeadingSection,
    bodyLarge = VfTypography.BodyDefault,
    bodyMedium = VfTypography.BodyItem,
    labelLarge = VfTypography.LabelCaps,
    labelMedium = VfTypography.MetaSub,
    labelSmall = VfTypography.MetaSubSm,
)
