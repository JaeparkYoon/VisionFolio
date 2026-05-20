package jpyoon.example.visionfolio.data.portfolio.db.dividend

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.data.portfolio.db.holding.HoldingEntity

@Entity(
    tableName = "dividend_cache",
    foreignKeys = [
        ForeignKey(
            entity = HoldingEntity::class,
            parentColumns = ["id"],
            childColumns = ["holdingId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("holdingId")],
)
data class DividendCacheEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val holdingId: String,
    val stockName: String,
    val stockCode: String,
    val dividendPerShare: Double,
    val paymentDate: String,
    val currency: String,
    val quantity: Double,
    val cachedAt: Long,
    val marketYieldPercent: Double = 0.0,
    val annualYieldPercent: Double = 0.0,
)
