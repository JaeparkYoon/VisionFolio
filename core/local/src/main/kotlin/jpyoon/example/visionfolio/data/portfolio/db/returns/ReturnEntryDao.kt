package jpyoon.example.visionfolio.data.portfolio.db.returns

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReturnEntryDao {

    @Query("SELECT * FROM return_entries WHERE year = :year ORDER BY month ASC")
    fun observeByYear(year: Int): Flow<List<ReturnEntryEntity>>

    @Query("SELECT * FROM return_entries ORDER BY year DESC, month DESC")
    fun observeAll(): Flow<List<ReturnEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ReturnEntryEntity)

    @Query("DELETE FROM return_entries WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM return_entries")
    suspend fun count(): Int
}
