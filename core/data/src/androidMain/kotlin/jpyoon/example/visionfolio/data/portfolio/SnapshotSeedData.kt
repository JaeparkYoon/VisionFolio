package jpyoon.example.visionfolio.data.portfolio

import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotEntity
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.snapshotEntityOf
import jpyoon.example.visionfolio.domain.model.AssetCategory
import java.time.Instant
import java.time.ZoneId

object SnapshotSeedData {

    private const val DAYS = 3650 // 10 years
    private const val START_TOTAL_KRW = 20_000_000L // 2천만

    private val CategoryRatios: Map<AssetCategory, Double> = mapOf(
        AssetCategory.DOMESTIC_STOCK to 0.22,
        AssetCategory.OVERSEAS_STOCK to 0.23,
        AssetCategory.ETF to 0.04,
        AssetCategory.CRYPTO to 0.24,
        AssetCategory.BOND to 0.05,
        AssetCategory.CASH to 0.08,
        AssetCategory.PENSION to 0.14,
    )

    fun generate(zone: ZoneId = ZoneId.systemDefault()): List<SnapshotEntity> {
        val rng = java.util.Random(2024L)
        val nowStart = Instant.now().atZone(zone).toLocalDate().atStartOfDay(zone)

        var price = START_TOTAL_KRW.toDouble()
        var vol = 0.010

        return (DAYS downTo 0).map { daysAgo ->
            val dayIndex = DAYS - daysAgo
            val timestamp = nowStart.minusDays(daysAgo.toLong()).toInstant().toEpochMilli()

            // 2천만 → 우상향 (base drift ≈ 0.00435)
            //   Crash 1 (~Year 2.5):  ~22% drop
            //   Crash 2 (~Year 5.2):  ~34% drop (severe)
            //   Crash 3 (~1 year ago): ~25% drop → recovery → 현재 우상향
            val (drift, volScale) = when (dayIndex) {
                in 900..950   -> -0.005 to 2.0
                in 951..1080  ->  0.003 to 1.3
                in 1900..1960 -> -0.007 to 2.3
                in 1961..2120 ->  0.004 to 1.3
                in 3250..3300 -> -0.006 to 2.0
                in 3301..3430 ->  0.003 to 1.3
                else          ->  0.00435 to 1.0
            }

            vol += rng.nextGaussian() * 0.0005
            vol = vol.coerceIn(0.005, 0.018)

            val dailyReturn = drift + rng.nextGaussian() * vol * volScale
            price *= (1.0 + dailyReturn)
            price = price.coerceAtLeast(START_TOTAL_KRW * 0.3)

            val total = price.toLong()
            val byCategory = CategoryRatios.mapValues { (_, ratio) -> (total * ratio).toLong() }
            snapshotEntityOf(timestamp = timestamp, totalKrw = total, byCategory = byCategory)
        }
    }
}
