package jpyoon.example.visionfolio.data.portfolio.db.importsource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImportSourceDao {

    @Query("SELECT * FROM import_sources")
    suspend fun getAll(): List<ImportSourceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ImportSourceEntity)
}
