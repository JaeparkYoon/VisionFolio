package jpyoon.example.visionfolio.domain.model

data class MarketIndex(
    val name: String,
    val value: Double,
    val changePct: Double,
    val spark: List<Double>,
)

data class NewsItem(
    val id: String,
    val tag: String,
    val title: String,
    val source: String,
    val publishedAt: Long,
    val aiSummary: String? = null,
)

data class Quote(
    val text: String,
    val author: String = "워런 버핏",
)
