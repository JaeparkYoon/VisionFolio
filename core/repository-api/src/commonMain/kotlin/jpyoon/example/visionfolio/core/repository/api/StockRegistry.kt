package jpyoon.example.visionfolio.core.repository.api

/**
 * 종목 마스터 엔트리.
 */
data class StockEntry(
    val code: String,
    val name: String,
)

/**
 * 국내(KRW) 종목 마스터 룩업.
 */
interface KrStockLookup {
    fun findByCode(code: String): StockEntry?
    fun findByName(name: String): StockEntry?
    fun isValid(name: String, code: String): Boolean
}

/**
 * 해외(USD) 종목 마스터 룩업.
 */
interface UsStockLookup {
    fun findByCode(code: String): StockEntry?
    fun isValid(name: String, code: String): Boolean
}
