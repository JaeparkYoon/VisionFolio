package jpyoon.example.visionfolio.data.portfolio.db.returns

import androidx.room.Entity
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.domain.model.ReturnCategory
import jpyoon.example.visionfolio.domain.model.ReturnEntry

@Entity(tableName = "return_entries")
data class ReturnEntryEntity(
    @PrimaryKey val id: String,
    val year: Int,
    val month: Int,
    val category: String,
    val amount: Long,
    val note: String = "",
    val createdAt: Long = System.currentTimeMillis(),
)

fun ReturnEntryEntity.toDomain(): ReturnEntry = ReturnEntry(
    id = id,
    year = year,
    month = month,
    category = runCatching { ReturnCategory.valueOf(category) }.getOrDefault(ReturnCategory.OTHER),
    amount = amount,
    note = note,
    createdAt = createdAt,
)

fun ReturnEntry.toEntity(): ReturnEntryEntity = ReturnEntryEntity(
    id = id,
    year = year,
    month = month,
    category = category.name,
    amount = amount,
    note = note,
    createdAt = createdAt,
)
