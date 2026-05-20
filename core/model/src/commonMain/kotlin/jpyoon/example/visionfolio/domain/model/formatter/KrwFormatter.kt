package jpyoon.example.visionfolio.domain.model.formatter

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
        abs >= 1_000_000_000_000L -> "${formatGroupedOneDecimal(abs / 1_000_000_000_000.0)}조"
        abs >= 100_000_000L       -> "${formatGroupedOneDecimal(abs / 100_000_000.0)}억"
        abs >= 10_000L            -> "${abs / 10_000}만"
        else -> "${abs}원"
    }
}
