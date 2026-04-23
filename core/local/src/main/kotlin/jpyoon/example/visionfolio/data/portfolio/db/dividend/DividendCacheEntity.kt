package jpyoon.example.visionfolio.data.portfolio.db.dividend

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dividend_cache")
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
)
