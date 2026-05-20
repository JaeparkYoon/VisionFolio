package jpyoon.example.visionfolio.data.portfolio.db.returns

import androidx.room.Entity
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.domain.model.ReturnCategory
import jpyoon.example.visionfolio.domain.model.ReturnEntry

@Entity(tableName = "return_entries")
data class ReturnEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val label: String,
    val investedAmount: Long,
    val currentValue: Long,
    val year: Int,
    val memo: String = "",
)

fun ReturnEntryEntity.toDomain(): ReturnEntry = ReturnEntry(
    id = id,
    category = runCatching { ReturnCategory.valueOf(category) }.getOrDefault(ReturnCategory.OTHER),
    label = label,
    investedAmount = investedAmount,
    currentValue = currentValue,
    year = year,
    memo = memo,
)

fun ReturnEntry.toEntity(): ReturnEntryEntity = ReturnEntryEntity(
    id = id,
    category = category.name,
    label = label,
    investedAmount = investedAmount,
    currentValue = currentValue,
    year = year,
    memo = memo,
)
