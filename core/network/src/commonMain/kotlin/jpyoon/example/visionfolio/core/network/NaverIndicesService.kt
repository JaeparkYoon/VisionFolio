package jpyoon.example.visionfolio.core.network

import kotlinx.serialization.Serializable

/**
 * Naver Finance domestic index chart (unofficial, no auth).
 * Returns mock data instead of real HTTP calls.
 */
class NaverIndicesService {
    suspend fun chart(
        symbol: String,
        periodType: String = "dayCandle",
        count: Int = 30,
    ): NaverIndexChartResponse = NaverIndexChartResponse(
        chartCode = symbol,
        priceInfos = listOf(
            NaverIndexPriceInfo(
                localDate = "20250113",
                openPrice = 2480.0,
                highPrice = 2510.0,
                lowPrice = 2470.0,
                closePrice = 2505.0,
            ),
            NaverIndexPriceInfo(
                localDate = "20250114",
                openPrice = 2505.0,
                highPrice = 2530.0,
                lowPrice = 2495.0,
                closePrice = 2520.0,
            ),
            NaverIndexPriceInfo(
                localDate = "20250115",
                openPrice = 2520.0,
                highPrice = 2545.0,
                lowPrice = 2510.0,
                closePrice = 2535.0,
            ),
        ),
    )
}

@Serializable
data class NaverIndexChartResponse(
    val chartCode: String? = null,
    val priceInfos: List<NaverIndexPriceInfo>? = null,
)

@Serializable
data class NaverIndexPriceInfo(
    val localDate: String? = null,
    val openPrice: Double? = null,
    val highPrice: Double? = null,
    val lowPrice: Double? = null,
    val closePrice: Double? = null,
)
