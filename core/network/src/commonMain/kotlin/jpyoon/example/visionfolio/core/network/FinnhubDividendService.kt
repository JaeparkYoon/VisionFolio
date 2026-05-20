package jpyoon.example.visionfolio.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Finnhub -- stock basic metrics API.
 * Returns mock data instead of real HTTP calls.
 */
class FinnhubDividendService {
    suspend fun getMetric(
        symbol: String,
        token: String,
        metric: String = "all",
    ): FinnhubMetricResponse = FinnhubMetricResponse(
        symbol = symbol,
        metric = FinnhubMetric(
            dividendPerShareAnnual = 3.28,
            dividendPerShareTTM = 3.28,
            dividendIndicatedAnnual = 3.40,
            currentDividendYieldTTM = 1.45,
            dividendYieldIndicatedAnnual = 1.50,
        ),
    )
}

@Serializable
data class FinnhubMetricResponse(
    val symbol: String = "",
    val metric: FinnhubMetric = FinnhubMetric(),
)

@Serializable
data class FinnhubMetric(
    @SerialName("dividendPerShareAnnual") val dividendPerShareAnnual: Double = 0.0,
    @SerialName("dividendPerShareTTM") val dividendPerShareTTM: Double = 0.0,
    @SerialName("dividendIndicatedAnnual") val dividendIndicatedAnnual: Double = 0.0,
    @SerialName("currentDividendYieldTTM") val currentDividendYieldTTM: Double = 0.0,
    @SerialName("dividendYieldIndicatedAnnual") val dividendYieldIndicatedAnnual: Double = 0.0,
)
