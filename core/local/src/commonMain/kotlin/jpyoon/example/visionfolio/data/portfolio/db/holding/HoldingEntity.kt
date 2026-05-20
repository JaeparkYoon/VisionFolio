package jpyoon.example.visionfolio.data.portfolio.db.holding

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.domain.model.Sector

@Entity(tableName = "holdings")
data class HoldingEntity(
    @PrimaryKey val id: String,
    val category: String,
    val name: String,
    val code: String,
    val quantity: Double,
    val currentValue: Double,
    val currency: String,
    val maturityDate: String? = null,
    val source: String = "",
    val sector: String? = null,
    @ColumnInfo(defaultValue = "0")
    val excludedFromAllocation: Boolean = false,
)

fun HoldingEntity.toDomain(): Holding = Holding(
    id = id,
    category = AssetCategory.valueOf(category),
    name = name,
    code = code,
    quantity = quantity,
    currentValue = currentValue,
    currency = Currency.valueOf(currency),
    maturityDate = maturityDate,
    source = source,
    sector = sector?.let { runCatching { Sector.valueOf(it) }.getOrNull() },
    excludedFromAllocation = excludedFromAllocation,
)

fun Holding.toEntity(): HoldingEntity = HoldingEntity(
    id = id,
    category = category.name,
    name = name,
    code = code,
    quantity = quantity,
    currentValue = currentValue,
    currency = currency.name,
    maturityDate = maturityDate,
    source = source,
    sector = sector?.name,
    excludedFromAllocation = excludedFromAllocation,
)
