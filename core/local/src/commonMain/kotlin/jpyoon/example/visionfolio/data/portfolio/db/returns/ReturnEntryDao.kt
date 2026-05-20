package jpyoon.example.visionfolio.data.portfolio.db.returns

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReturnEntryDao {

    @Query("SELECT * FROM return_entries ORDER BY year DESC, id DESC")
    fun observeAll(): Flow<List<ReturnEntryEntity>>

    @Query("SELECT * FROM return_entries WHERE year = :year ORDER BY id DESC")
    fun observeByYear(year: Int): Flow<List<ReturnEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ReturnEntryEntity)

    @Update
    suspend fun update(entity: ReturnEntryEntity)

    @Query("DELETE FROM return_entries WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM return_entries")
    suspend fun clear()
}
