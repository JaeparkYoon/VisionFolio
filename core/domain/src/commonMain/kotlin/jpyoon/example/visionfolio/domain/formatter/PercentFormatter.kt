package jpyoon.example.visionfolio.domain.formatter

import kotlin.math.absoluteValue

object PercentFormatter {

    fun format(pct: Double, alwaysSign: Boolean = true): String {
        if (pct == 0.0) return "0.00%"
        val sign = when {
            pct > 0 && alwaysSign -> "+"
            pct < 0 -> "-"
            else -> ""
        }
        return "$sign${formatTwoDecimal(pct.absoluteValue)}%"
    }

    private fun formatTwoDecimal(value: Double): String {
        val intPart = value.toLong()
        val fracPart = ((value - intPart) * 100 + 0.5).toLong()
        return "$intPart.${fracPart.toString().padStart(2, '0')}"
    }
}
