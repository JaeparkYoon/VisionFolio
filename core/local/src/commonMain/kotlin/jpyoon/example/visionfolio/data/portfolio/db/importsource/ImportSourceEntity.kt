package jpyoon.example.visionfolio.data.portfolio.db.importsource

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "import_sources")
data class ImportSourceEntity(
    @PrimaryKey val label: String,
    val fingerprint: Long,
)
