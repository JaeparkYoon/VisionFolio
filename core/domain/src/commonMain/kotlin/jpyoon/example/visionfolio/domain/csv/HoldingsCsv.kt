package jpyoon.example.visionfolio.domain.csv

import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.Holding

/** RFC 4180 느슨한 준수. 쉼표/따옴표/개행만 escape */
object HoldingsCsv {

    private const val HEADER = "id,category,name,code,quantity,avgPrice,currentPrice,currency,maturityDate"

    fun encode(holdings: List<Holding>): String = buildString {
        appendLine(HEADER)
        holdings.forEach { h ->
            appendLine(
                listOf(
                    h.id,
                    h.category.name,
                    h.name,
                    h.code,
                    h.quantity.toString(),
                    h.avgPrice.toString(),
                    h.currentPrice.toString(),
                    h.currency.name,
                    h.maturityDate.orEmpty(),
                ).joinToString(",") { escape(it) }
            )
        }
    }

    fun decode(text: String): List<Holding> {
        val lines = text.lineSequence().filter { it.isNotBlank() }.toList()
        if (lines.isEmpty()) return emptyList()
        val dataLines = if (lines.first().startsWith("id,category")) lines.drop(1) else lines
        return dataLines.mapNotNull { parseLine(it) }
    }

    private fun parseLine(line: String): Holding? {
        val fields = splitCsv(line)
        if (fields.size < 8) return null
        return runCatching {
            Holding(
                id = fields[0],
                category = AssetCategory.valueOf(fields[1]),
                name = fields[2],
                code = fields[3],
                quantity = fields[4].toDouble(),
                avgPrice = fields[5].toDouble(),
                currentPrice = fields[6].toDouble(),
                currency = Currency.valueOf(fields[7]),
                maturityDate = fields.getOrNull(8)?.ifBlank { null },
            )
        }.getOrNull()
    }

    private fun escape(value: String): String {
        val needsQuote = value.contains(',') || value.contains('"') || value.contains('\n')
        val escaped = value.replace("\"", "\"\"")
        return if (needsQuote) "\"$escaped\"" else escaped
    }

    private fun splitCsv(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            when {
                c == '"' && inQuotes && i + 1 < line.length && line[i + 1] == '"' -> {
                    current.append('"'); i++
                }
                c == '"' -> inQuotes = !inQuotes
                c == ',' && !inQuotes -> {
                    result.add(current.toString()); current.clear()
                }
                else -> current.append(c)
            }
            i++
        }
        result.add(current.toString())
        return result
    }
}
