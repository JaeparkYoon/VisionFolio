package jpyoon.example.visionfolio.domain.formatter

import kotlin.math.absoluteValue

object KrwFormatter {

    fun format(amount: Long, compact: Boolean = false, withSign: Boolean = false): String {
        val sign = when {
            !withSign -> ""
            amount > 0 -> "+"
            amount < 0 -> "-"
            else -> ""
        }
        val abs = amount.absoluteValue
        val body = if (compact) compact(abs) else "${formatGrouped(abs)}원"
        return "$sign$body"
    }

    private fun compact(abs: Long): String = when {
        abs >= 100_000_000L -> "${formatOneDecimal(abs / 100_000_000.0)}억원"
        abs >= 10_000L -> "${formatGrouped(abs / 10_000L)}만원"
        else -> "${formatGrouped(abs)}원"
    }

    private fun formatGrouped(value: Long): String {
        val str = value.toString()
        val result = StringBuilder()
        var count = 0
        for (i in str.length - 1 downTo 0) {
            if (count > 0 && count % 3 == 0) result.insert(0, ',')
            result.insert(0, str[i])
            count++
        }
        return result.toString()
    }

    private fun formatOneDecimal(value: Double): String {
        val intPart = value.toLong()
        val fracPart = ((value - intPart) * 10 + 0.5).toLong()
        return if (fracPart == 0L) formatGrouped(intPart) else "${formatGrouped(intPart)}.$fracPart"
    }
}
