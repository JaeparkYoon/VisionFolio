package jpyoon.example.visionfolio.data.portfolio.db.snapshot

import androidx.room.Entity
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.domain.model.AssetCategory

@Entity(tableName = "portfolio_snapshots")
data class SnapshotEntity(
    @PrimaryKey val timestamp: Long,
    val totalKrw: Long,
    val categoriesJson: String,
)

fun SnapshotEntity.categories(): Map<AssetCategory, Long> = CategoriesJson.decode(categoriesJson)

fun snapshotEntityOf(
    timestamp: Long,
    totalKrw: Long,
    byCategory: Map<AssetCategory, Long>,
): SnapshotEntity = SnapshotEntity(
    timestamp = timestamp,
    totalKrw = totalKrw,
    categoriesJson = CategoriesJson.encode(byCategory),
)
