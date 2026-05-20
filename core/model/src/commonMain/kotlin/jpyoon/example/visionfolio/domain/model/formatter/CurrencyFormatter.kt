package jpyoon.example.visionfolio.domain.model.formatter

import jpyoon.example.visionfolio.domain.model.Currency
import kotlin.math.absoluteValue
import kotlin.math.roundToLong

/**
 * 통화 표시 포매터.
 *
 * - **KRW**: 원화는 정수 금액을 기본 단위로 다룬다. compact=true 일 때 "조/억/만" 한국식 단위로 축약.
 * - **USD**: 비-compact 일 때는 항상 소수점 둘째 자리(cents)까지 표시한다 — 예: `$1,890.00`.
 *   compact 모드에서도 1,000 미만 금액은 `$X.XX` 형태로 cents를 보존하고, 그 이상은 `K/M/B`로 축약.
 */
object CurrencyFormatter {

    fun format(
        amount: Long,
        currency: Currency,
        compact: Boolean = false,
        withSign: Boolean = false,
    ): String = format(amount.toDouble(), currency, compact, withSign)

    fun format(
        amount: Double,
        currency: Currency,
        compact: Boolean = false,
        withSign: Boolean = false,
    ): String {
        return when (currency) {
            Currency.KRW -> KrwFormatter.format(amount.roundToLong(), compact, withSign)
            Currency.USD -> formatUsd(amount, compact, withSign)
        }
    }

    private fun formatUsd(amount: Double, compact: Boolean, withSign: Boolean): String {
        val sign = when {
            !withSign -> ""
            amount > 0.0 -> "+"
            amount < 0.0 -> "-"
            else -> ""
        }
        val abs = amount.absoluteValue
        val body = if (compact) compactUsd(abs) else "$${formatGroupedTwoDecimal(abs)}"
        return "$sign$body"
    }

    private fun compactUsd(abs: Double): String = when {
        abs >= 1_000_000_000.0 -> "$${formatGroupedOneDecimal(abs / 1_000_000_000.0)}B"
        abs >= 1_000_000.0     -> "$${formatGroupedOneDecimal(abs / 1_000_000.0)}M"
        abs >= 1_000.0         -> "$${formatGrouped((abs / 1_000.0).roundToLong())}K"
        else -> "$${formatGroupedTwoDecimal(abs)}"
    }
}
