package jpyoon.example.visionfolio.domain.model

enum class Sector(val label: String) {
    TECHNOLOGY("기술"),
    FINANCE("금융"),
    HEALTHCARE("헬스케어"),
    CONSUMER("소비재"),
    INDUSTRIAL("산업재"),
    ENERGY("에너지"),
    MATERIALS("소재"),
    UTILITIES("유틸리티"),
    REAL_ESTATE("부동산"),
    COMMUNICATION("커뮤니케이션"),
    OTHER("기타"),
}

object SectorMap {
    private val map = mapOf(
        "005930" to Sector.TECHNOLOGY,  // 삼성전자
        "000660" to Sector.TECHNOLOGY,  // SK하이닉스
        "035420" to Sector.TECHNOLOGY,  // NAVER
        "035720" to Sector.TECHNOLOGY,  // 카카오
        "105560" to Sector.FINANCE,     // KB금융
        "055550" to Sector.FINANCE,     // 신한지주
        "005380" to Sector.INDUSTRIAL,  // 현대차
        "AAPL" to Sector.TECHNOLOGY,
        "MSFT" to Sector.TECHNOLOGY,
        "GOOGL" to Sector.COMMUNICATION,
        "AMZN" to Sector.CONSUMER,
        "TSLA" to Sector.CONSUMER,
        "NVDA" to Sector.TECHNOLOGY,
        "META" to Sector.COMMUNICATION,
        "JPM" to Sector.FINANCE,
        "JNJ" to Sector.HEALTHCARE,
        "XOM" to Sector.ENERGY,
    )

    fun of(code: String): Sector? = map[code.uppercase()]
}
