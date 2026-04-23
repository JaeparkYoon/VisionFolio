package jpyoon.example.visionfolio.domain.formatter

import java.text.DecimalFormat
import kotlin.math.absoluteValue

object PercentFormatter {

    private val twoDecimal = DecimalFormat("0.00")

    fun format(pct: Double, alwaysSign: Boolean = true): String {
        if (pct == 0.0) return "0.00%"
        val sign = when {
            pct > 0 && alwaysSign -> "+"
            pct < 0 -> "-"
            else -> ""
        }
        return "$sign${twoDecimal.format(pct.absoluteValue)}%"
    }
}
