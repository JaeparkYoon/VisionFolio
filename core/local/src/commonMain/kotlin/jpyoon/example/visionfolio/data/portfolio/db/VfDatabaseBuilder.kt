package jpyoon.example.visionfolio.data.portfolio.db

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver

fun createVfDatabaseBuilder(builder: RoomDatabase.Builder<VfDatabase>): VfDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
}
