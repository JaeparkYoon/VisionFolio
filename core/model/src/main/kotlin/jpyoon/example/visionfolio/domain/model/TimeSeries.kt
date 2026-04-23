package jpyoon.example.visionfolio.domain.model

enum class Period { D1, W1, M1, M3, M6, Y1, ALL, CUSTOM }

data class SeriesPoint(val timestamp: Long, val valueKrw: Long)

data class PortfolioSeries(
    val period: Period,
    val points: List<SeriesPoint>,
)

data class IntervalStats(
    val min: Long,
    val max: Long,
    val avg: Long,
    val start: Long,
    val end: Long,
    val changeKrw: Long,
    val changePct: Double,
)

data class CategoryContribution(
    val category: AssetCategory,
    val changePct: Double,
    val changeKrw: Long,
)
