package jpyoon.example.visionfolio.data.portfolio.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import jpyoon.example.visionfolio.data.portfolio.db.chat.ChatDao
import jpyoon.example.visionfolio.data.portfolio.db.chat.ChatMessageEntity
import jpyoon.example.visionfolio.data.portfolio.db.chat.ChatSessionEntity
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendCacheDao
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendCacheEntity
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendOverrideDao
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendOverrideEntity
import jpyoon.example.visionfolio.data.portfolio.db.holding.HoldingDao
import jpyoon.example.visionfolio.data.portfolio.db.holding.HoldingEntity
import jpyoon.example.visionfolio.data.portfolio.db.importsource.ImportSourceDao
import jpyoon.example.visionfolio.data.portfolio.db.importsource.ImportSourceEntity
import jpyoon.example.visionfolio.data.portfolio.db.returns.ReturnEntryDao
import jpyoon.example.visionfolio.data.portfolio.db.returns.ReturnEntryEntity
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotDao
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotEntity

@Database(
    entities = [
        HoldingEntity::class,
        SnapshotEntity::class,
        ImportSourceEntity::class,
        DividendCacheEntity::class,
        DividendOverrideEntity::class,
        ChatSessionEntity::class,
        ChatMessageEntity::class,
        ReturnEntryEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@ConstructedBy(VfDatabaseConstructor::class)
abstract class VfDatabase : RoomDatabase() {
    abstract fun holdingDao(): HoldingDao
    abstract fun snapshotDao(): SnapshotDao
    abstract fun importSourceDao(): ImportSourceDao
    abstract fun dividendCacheDao(): DividendCacheDao
    abstract fun dividendOverrideDao(): DividendOverrideDao
    abstract fun chatDao(): ChatDao
    abstract fun returnEntryDao(): ReturnEntryDao
}

/**
 * Room KMP -- actual is auto-generated at compile time. Only expect is declared.
 */
@Suppress("KotlinNoActualForExpect")
expect object VfDatabaseConstructor : RoomDatabaseConstructor<VfDatabase> {
    override fun initialize(): VfDatabase
}

internal const val VF_DATABASE_NAME = "visionfolio.db"
