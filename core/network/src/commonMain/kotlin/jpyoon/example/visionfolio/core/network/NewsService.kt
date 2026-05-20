package jpyoon.example.visionfolio.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * NewsAPI.org -- top-headlines endpoint.
 * Returns mock data instead of real HTTP calls.
 */
class NewsService {
    suspend fun topHeadlines(
        apiKey: String,
        country: String = "us",
        category: String = "business",
        pageSize: Int = 5,
    ): NewsResponse = NewsResponse(
        status = "ok",
        totalResults = 3,
        articles = listOf(
            NewsArticle(
                title = "Markets Rally on Positive Economic Data",
                url = "https://example.com/news/1",
                publishedAt = "2025-01-15T10:30:00Z",
                source = NewsSource(name = "Mock Financial Times"),
                description = "Stock markets surged today on better-than-expected jobs data.",
            ),
            NewsArticle(
                title = "Tech Sector Leads Gains in Morning Trading",
                url = "https://example.com/news/2",
                publishedAt = "2025-01-15T09:00:00Z",
                source = NewsSource(name = "Mock Reuters"),
                description = "Technology stocks posted strong gains in early trading session.",
            ),
            NewsArticle(
                title = "Federal Reserve Signals Steady Interest Rates",
                url = "https://example.com/news/3",
                publishedAt = "2025-01-14T18:00:00Z",
                source = NewsSource(name = "Mock Bloomberg"),
                description = "The Federal Reserve indicated it will maintain current rate levels.",
            ),
        ),
    )
}

@Serializable
data class NewsResponse(
    val status: String = "",
    val totalResults: Int = 0,
    val articles: List<NewsArticle> = emptyList(),
)

@Serializable
data class NewsArticle(
    val title: String = "",
    val url: String = "",
    val publishedAt: String = "",
    val source: NewsSource = NewsSource(),
    @SerialName("description") val description: String? = null,
)

@Serializable
data class NewsSource(val name: String = "")
