package jpyoon.example.visionfolio.data.portfolio.db.dividend

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dividend_override")
data class DividendOverrideEntity(
    @PrimaryKey val holdingId: String,
    val dividendPerShare: Double?,
    val dividendYield: Double?,
    val frequency: String?,
    val currency: String?,
    val updatedAt: Long,
)
