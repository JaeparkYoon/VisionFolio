package jpyoon.example.visionfolio.domain.formatter

import java.text.DecimalFormat
import kotlin.math.absoluteValue

object KrwFormatter {

    private val grouped = DecimalFormat("#,##0")
    private val oneDecimal = DecimalFormat("#,##0.#")

    fun format(amount: Long, compact: Boolean = false, withSign: Boolean = false): String {
        val sign = when {
            !withSign -> ""
            amount > 0 -> "+"
            amount < 0 -> "-"
            else -> ""
        }
        val abs = amount.absoluteValue
        val body = if (compact) compact(abs) else "${grouped.format(abs)}원"
        return "$sign$body"
    }

    private fun compact(abs: Long): String = when {
        abs >= 100_000_000L -> "${oneDecimal.format(abs / 100_000_000.0)}억원"
        abs >= 10_000L -> "${grouped.format(abs / 10_000L)}만원"
        else -> "${grouped.format(abs)}원"
    }
}
