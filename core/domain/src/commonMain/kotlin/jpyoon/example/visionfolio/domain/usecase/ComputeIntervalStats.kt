package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.IntervalStats
import jpyoon.example.visionfolio.domain.model.PortfolioSeries

object ComputeIntervalStats {

    fun invoke(series: PortfolioSeries): IntervalStats? {
        if (series.points.isEmpty()) return null
        val values = series.points.map { it.valueKrw }
        val start = values.first()
        val end = values.last()
        val change = end - start
        val pct = if (start == 0L) 0.0 else change.toDouble() / start.toDouble() * 100.0
        return IntervalStats(
            min = values.min(),
            max = values.max(),
            avg = values.average().toLong(),
            start = start,
            end = end,
            changeKrw = change,
            changePct = pct,
        )
    }
}
