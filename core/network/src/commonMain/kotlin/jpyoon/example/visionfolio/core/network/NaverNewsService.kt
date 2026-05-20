package jpyoon.example.visionfolio.core.network

import kotlinx.serialization.Serializable

/**
 * 네이버 검색 API -- 뉴스 엔드포인트.
 * Returns mock data instead of real HTTP calls.
 */
class NaverNewsService {
    suspend fun searchNews(
        clientId: String,
        clientSecret: String,
        query: String,
        display: Int = 100,
        start: Int = 1,
        sort: String = "date",
    ): NaverNewsResponse = NaverNewsResponse(
        total = 2,
        start = 1,
        display = 2,
        items = listOf(
            NaverNewsItem(
                title = "코스피, 외국인 매수에 상승 마감",
                originallink = "https://example.com/kr-news/1",
                link = "https://example.com/kr-news/1",
                description = "코스피 지수가 외국인 매수세에 힘입어 상승 마감했다.",
                pubDate = "Tue, 15 Jan 2025 18:30:00 +0900",
            ),
            NaverNewsItem(
                title = "삼성전자, 실적 개선 기대감에 강세",
                originallink = "https://example.com/kr-news/2",
                link = "https://example.com/kr-news/2",
                description = "삼성전자가 실적 개선 기대감으로 강세를 보였다.",
                pubDate = "Tue, 15 Jan 2025 17:00:00 +0900",
            ),
        ),
    )
}

@Serializable
data class NaverNewsResponse(
    val total: Int = 0,
    val start: Int = 0,
    val display: Int = 0,
    val items: List<NaverNewsItem> = emptyList(),
)

@Serializable
data class NaverNewsItem(
    val title: String = "",
    val originallink: String = "",
    val link: String = "",
    val description: String = "",
    val pubDate: String = "",
)
