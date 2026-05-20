package jpyoon.example.visionfolio.core.network

import kotlinx.serialization.Serializable

/**
 * Yahoo Finance v8 chart endpoint (unofficial, no auth).
 * Returns mock data instead of real HTTP calls.
 */
class YahooFinanceService {
    suspend fun chart(
        symbol: String,
        range: String = "1mo",
        interval: String = "1d",
    ): YahooChartResponse = YahooChartResponse(
        chart = YahooChart(
            result = listOf(
                YahooChartResult(
                    meta = YahooChartMeta(
                        symbol = symbol,
                        regularMarketPrice = 5970.25,
                        previousClose = 5940.10,
                        chartPreviousClose = 5800.00,
                    ),
                    timestamp = listOf(1736784000L, 1736870400L, 1736956800L),
                    indicators = YahooIndicators(
                        quote = listOf(
                            YahooQuoteSeries(
                                close = listOf(5910.00, 5940.00, 5970.25),
                            ),
                        ),
                    ),
                ),
            ),
        ),
    )

    /**
     * Dividend events query.
     * Returns mock dividend event data.
     */
    suspend fun dividendEvents(
        symbol: String,
        range: String = "2y",
        interval: String = "1mo",
        events: String = "div",
    ): YahooChartResponse = YahooChartResponse(
        chart = YahooChart(
            result = listOf(
                YahooChartResult(
                    meta = YahooChartMeta(
                        symbol = symbol,
                        regularMarketPrice = 185.50,
                        previousClose = 184.20,
                    ),
                    timestamp = listOf(1704067200L, 1711929600L, 1719792000L, 1727740800L),
                    indicators = YahooIndicators(
                        quote = listOf(
                            YahooQuoteSeries(
                                close = listOf(180.0, 182.5, 184.0, 185.5),
                            ),
                        ),
                    ),
                    events = YahooEvents(
                        dividends = mapOf(
                            "1707264000" to YahooDividendEvent(amount = 0.82, date = 1707264000L),
                            "1715126400" to YahooDividendEvent(amount = 0.82, date = 1715126400L),
                            "1722988800" to YahooDividendEvent(amount = 0.85, date = 1722988800L),
                            "1730851200" to YahooDividendEvent(amount = 0.85, date = 1730851200L),
                        ),
                    ),
                ),
            ),
        ),
    )
}

@Serializable
data class YahooChartResponse(
    val chart: YahooChart? = null,
)

@Serializable
data class YahooChart(
    val result: List<YahooChartResult>? = null,
    val error: YahooChartError? = null,
)

@Serializable
data class YahooChartError(
    val code: String? = null,
    val description: String? = null,
)

@Serializable
data class YahooChartResult(
    val meta: YahooChartMeta? = null,
    val timestamp: List<Long>? = null,
    val indicators: YahooIndicators? = null,
    val events: YahooEvents? = null,
)

@Serializable
data class YahooEvents(
    val dividends: Map<String, YahooDividendEvent>? = null,
)

@Serializable
data class YahooDividendEvent(
    val amount: Double = 0.0,
    val date: Long = 0L,
)

@Serializable
data class YahooChartMeta(
    val symbol: String? = null,
    val regularMarketPrice: Double? = null,
    val previousClose: Double? = null,
    val chartPreviousClose: Double? = null,
)

@Serializable
data class YahooIndicators(
    val quote: List<YahooQuoteSeries>? = null,
)

@Serializable
data class YahooQuoteSeries(
    val close: List<Double?>? = null,
)
