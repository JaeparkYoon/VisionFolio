package jpyoon.example.visionfolio.data.market

import jpyoon.example.visionfolio.domain.model.MarketIndex
import jpyoon.example.visionfolio.domain.model.NewsItem

/**
 * Mock 시장 데이터. 오픈소스 빌드에서는 모든 경로가 이 데이터를 사용한다.
 */
object FakeMarketData {

    val Indices: List<MarketIndex> = listOf(
        MarketIndex("KOSPI", 2_714.53, 0.83, listOf(2680.0, 2694.0, 2688.0, 2705.0, 2714.0)),
        MarketIndex("KOSDAQ", 867.88, -0.24, listOf(870.0, 872.0, 868.0, 866.0, 867.0)),
        MarketIndex("S&P 500", 5_212.42, 0.42, listOf(5170.0, 5180.0, 5175.0, 5199.0, 5212.0)),
        MarketIndex("NASDAQ", 16_742.31, 0.56, listOf(16500.0, 16590.0, 16650.0, 16700.0, 16742.0)),
        MarketIndex("BTC/KRW", 95_820_000.0, 1.82, listOf(94_000_000.0, 94_500_000.0, 95_100_000.0, 95_820_000.0)),
    )

    val News: List<NewsItem> = listOf(
        NewsItem("n1", "시황", "코스피, 외국인 매수 전환에 2,710선 회복", "연합뉴스", 0L,
            aiSummary = "외국인 순매수 전환으로 단기 반등 기대감 상승"),
        NewsItem("n2", "해외", "엔비디아, AI 가속기 수요 강세에 장중 최고치", "Bloomberg", 0L,
            aiSummary = "AI 반도체 수요 확대가 엔비디아 실적 견인 전망"),
        NewsItem("n3", "암호화폐", "비트코인, 반감기 이후 9.5만 달러 돌파", "CoinDesk", 0L,
            aiSummary = "반감기 공급 축소 효과로 중장기 상승 모멘텀 유지"),
        NewsItem("n4", "정책", "금통위, 기준금리 동결…인하 시점은 불투명", "매일경제", 0L,
            aiSummary = "금리 동결 장기화 시 성장주보다 가치주 선호 예상"),
        NewsItem("n5", "환율", "원/달러, 연고점 부근 1,388원…수급 경계감", "조선비즈", 0L,
            aiSummary = "원화 약세 지속 시 수출주 수혜, 수입 비중 높은 기업 부담"),
    )
}
