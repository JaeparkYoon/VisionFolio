package jpyoon.example.visionfolio.data.portfolio.db

import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendCacheDao
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendCacheEntity
import jpyoon.example.visionfolio.data.portfolio.db.holding.HoldingDao
import jpyoon.example.visionfolio.data.portfolio.db.holding.HoldingEntity
import jpyoon.example.visionfolio.data.portfolio.db.importsource.ImportSourceDao
import jpyoon.example.visionfolio.data.portfolio.db.importsource.ImportSourceEntity
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotDao
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotEntity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        HoldingEntity::class,
        SnapshotEntity::class,
        ImportSourceEntity::class,
        DividendCacheEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class VfDatabase : RoomDatabase() {
    abstract fun holdingDao(): HoldingDao
    abstract fun snapshotDao(): SnapshotDao
    abstract fun importSourceDao(): ImportSourceDao
    abstract fun dividendCacheDao(): DividendCacheDao

    companion object {
        private const val DB_NAME = "visionfolio.db"

        @Volatile private var instance: VfDatabase? = null

        fun get(context: Context): VfDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                VfDatabase::class.java,
                DB_NAME,
            )
                .build()
                .also { instance = it }
        }
    }
}
