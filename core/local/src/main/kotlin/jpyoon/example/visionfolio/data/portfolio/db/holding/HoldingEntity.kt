package jpyoon.example.visionfolio.data.portfolio.db.holding

import androidx.room.Entity
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.Holding

@Entity(tableName = "holdings")
data class HoldingEntity(
    @PrimaryKey val id: String,
    val category: String,
    val name: String,
    val code: String,
    val quantity: Double,
    val avgPrice: Double,
    val currentPrice: Double,
    val currency: String,
    val maturityDate: String? = null,
    val source: String = "",
)

fun HoldingEntity.toDomain(): Holding = Holding(
    id = id,
    category = AssetCategory.valueOf(category),
    name = name,
    code = code,
    quantity = quantity,
    avgPrice = avgPrice,
    currentPrice = currentPrice,
    currency = Currency.valueOf(currency),
    maturityDate = maturityDate,
    source = source,
)

fun Holding.toEntity(): HoldingEntity = HoldingEntity(
    id = id,
    category = category.name,
    name = name,
    code = code,
    quantity = quantity,
    avgPrice = avgPrice,
    currentPrice = currentPrice,
    currency = currency.name,
    maturityDate = maturityDate,
    source = source,
)
