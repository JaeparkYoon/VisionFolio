package jpyoon.example.visionfolio.domain.model.formatter

import java.text.DecimalFormat

private val grouped = DecimalFormat("#,##0")
private val groupedOneDecimal = DecimalFormat("#,##0.#")
private val groupedTwoDecimal = DecimalFormat("#,##0.00")
private val twoDecimal = DecimalFormat("0.00")

actual fun formatGrouped(value: Long): String = grouped.format(value)

actual fun formatGroupedOneDecimal(value: Double): String = groupedOneDecimal.format(value)

actual fun formatTwoDecimal(value: Double): String = twoDecimal.format(value)

actual fun formatGroupedTwoDecimal(value: Double): String = groupedTwoDecimal.format(value)
