package jpyoon.example.visionfolio.data.market

import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.DividendRecord
import jpyoon.example.visionfolio.domain.model.Holding

/**
 * Mock 배당 데이터. 보유 종목 이름/코드 기준으로 몇 분기치 샘플을 반환한다.
 * 실제 API 연동이 제거된 오픈소스 빌드에서 사용된다.
 */
object FakeDividendData {

    fun forHolding(holding: Holding): List<DividendRecord> {
        val key = holding.code.ifBlank { holding.name }
        val quarters = Samples[key] ?: return emptyList()
        return quarters.map { (amount, date) ->
            DividendRecord(
                stockName = holding.name,
                stockCode = holding.code,
                dividendPerShare = amount,
                paymentDate = date,
                currency = holding.currency,
            )
        }
    }

    private val Samples: Map<String, List<Pair<Double, String>>> = mapOf(
        // 국내 주식 (KRW, 분기 배당)
        "005930" to listOf(
            361.0 to "2025-04-18",
            361.0 to "2025-01-17",
            361.0 to "2024-10-18",
            361.0 to "2024-07-19",
        ),
        "000660" to listOf(
            300.0 to "2025-04-25",
            200.0 to "2024-10-25",
        ),
        // 국내 ETF (KRW)
        "360750" to listOf(
            120.0 to "2025-04-30",
            110.0 to "2025-01-30",
            105.0 to "2024-10-30",
        ),
        "069500" to listOf(
            450.0 to "2025-04-30",
            400.0 to "2024-10-30",
        ),
        // 해외 주식 (USD, 분기 배당)
        "NVDA" to listOf(
            0.01 to "2025-03-28",
            0.01 to "2024-12-27",
            0.01 to "2024-09-27",
            0.01 to "2024-06-28",
        ),
        "AAPL" to listOf(
            0.25 to "2025-05-15",
            0.25 to "2025-02-13",
            0.24 to "2024-11-14",
            0.24 to "2024-08-15",
        ),
    )
}
