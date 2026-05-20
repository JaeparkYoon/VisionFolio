package jpyoon.example.visionfolio.core.network

import kotlinx.serialization.Serializable

/**
 * Twelve Data time series API.
 * Returns mock data instead of real HTTP calls.
 */
class TwelveDataService {
    suspend fun timeSeries(
        symbol: String,
        apiKey: String,
        interval: String = "1day",
        outputSize: Int = 30,
        order: String = "ASC",
    ): TwelveDataTimeSeriesResponse = TwelveDataTimeSeriesResponse(
        meta = TwelveDataMeta(
            symbol = symbol,
            interval = interval,
            currency = "USD",
            type = "Index",
        ),
        values = listOf(
            TwelveDataValue(
                datetime = "2025-01-13",
                open = "5880.00",
                high = "5920.00",
                low = "5870.00",
                close = "5910.00",
            ),
            TwelveDataValue(
                datetime = "2025-01-14",
                open = "5910.00",
                high = "5950.00",
                low = "5900.00",
                close = "5940.00",
            ),
            TwelveDataValue(
                datetime = "2025-01-15",
                open = "5940.00",
                high = "5980.00",
                low = "5930.00",
                close = "5970.00",
            ),
        ),
        status = "ok",
    )
}

@Serializable
data class TwelveDataTimeSeriesResponse(
    val meta: TwelveDataMeta? = null,
    val values: List<TwelveDataValue>? = null,
    val status: String? = null,
    val code: Int? = null,
    val message: String? = null,
)

@Serializable
data class TwelveDataMeta(
    val symbol: String? = null,
    val interval: String? = null,
    val currency: String? = null,
    val type: String? = null,
)

@Serializable
data class TwelveDataValue(
    val datetime: String? = null,
    val open: String? = null,
    val high: String? = null,
    val low: String? = null,
    val close: String? = null,
)
