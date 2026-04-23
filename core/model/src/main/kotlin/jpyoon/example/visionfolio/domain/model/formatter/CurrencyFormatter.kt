package jpyoon.example.visionfolio.domain.model.formatter

import jpyoon.example.visionfolio.domain.model.Currency
import java.text.DecimalFormat
import kotlin.math.absoluteValue

object CurrencyFormatter {

    private val grouped = DecimalFormat("#,##0")
    private val oneDecimal = DecimalFormat("#,##0.#")

    fun format(
        amount: Long,
        currency: Currency,
        compact: Boolean = false,
        withSign: Boolean = false,
    ): String {
        return when (currency) {
            Currency.KRW -> KrwFormatter.format(amount, compact, withSign)
            Currency.USD -> formatUsd(amount, compact, withSign)
        }
    }

    private fun formatUsd(amount: Long, compact: Boolean, withSign: Boolean): String {
        val sign = when {
            !withSign -> ""
            amount > 0 -> "+"
            amount < 0 -> "-"
            else -> ""
        }
        val abs = amount.absoluteValue
        val body = if (compact) compactUsd(abs) else "$${grouped.format(abs)}"
        return "$sign$body"
    }

    private fun compactUsd(abs: Long): String = when {
        abs >= 1_000_000_000L -> "$${oneDecimal.format(abs / 1_000_000_000.0)}B"
        abs >= 1_000_000L     -> "$${oneDecimal.format(abs / 1_000_000.0)}M"
        abs >= 1_000L         -> "$${abs / 1_000}K"
        else -> "$$abs"
    }
}
