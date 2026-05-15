package jpyoon.example.visionfolio.data.portfolio.db.dividendoverride

import androidx.room.Entity
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.domain.model.DividendOverride

@Entity(tableName = "dividend_overrides")
data class DividendOverrideEntity(
    @PrimaryKey val holdingId: String,
    val annualDividendKrw: Long,
    val updatedAt: Long = System.currentTimeMillis(),
)

fun DividendOverrideEntity.toDomain(): DividendOverride = DividendOverride(
    holdingId = holdingId,
    annualDividendKrw = annualDividendKrw,
    updatedAt = updatedAt,
)

fun DividendOverride.toEntity(): DividendOverrideEntity = DividendOverrideEntity(
    holdingId = holdingId,
    annualDividendKrw = annualDividendKrw,
    updatedAt = updatedAt,
)
