package jpyoon.example.visionfolio.data.portfolio.db.returns

import androidx.room.Entity
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.ReturnCategory
import jpyoon.example.visionfolio.domain.model.ReturnEntry

@Entity(tableName = "return_entries")
data class ReturnEntryEntity(
    @PrimaryKey val id: String,
    val date: String,
    val amount: Double,
    val currency: String,
    val category: String,
    val note: String? = null,
)

fun ReturnEntryEntity.toDomain(): ReturnEntry = ReturnEntry(
    id = id,
    date = date,
    amount = amount,
    currency = Currency.valueOf(currency),
    category = runCatching { ReturnCategory.valueOf(category) }.getOrDefault(ReturnCategory.OTHER),
    note = note,
)

fun ReturnEntry.toEntity(): ReturnEntryEntity = ReturnEntryEntity(
    id = id,
    date = date,
    amount = amount,
    currency = currency.name,
    category = category.name,
    note = note,
)
