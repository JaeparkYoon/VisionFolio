package jpyoon.example.visionfolio.data.portfolio.db.holding

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HoldingDao {

    @Query("SELECT * FROM holdings")
    fun observeAll(): Flow<List<HoldingEntity>>

    @Query("SELECT COUNT(*) FROM holdings")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(entities: List<HoldingEntity>)

    @Update
    suspend fun update(entity: HoldingEntity)

    @Query("DELETE FROM holdings WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM holdings")
    suspend fun clear()
}
