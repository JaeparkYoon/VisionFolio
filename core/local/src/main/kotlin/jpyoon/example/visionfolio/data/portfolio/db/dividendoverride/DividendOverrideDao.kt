package jpyoon.example.visionfolio.data.portfolio.db.dividendoverride

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DividendOverrideDao {

    @Query("SELECT * FROM dividend_overrides")
    fun observeAll(): Flow<List<DividendOverrideEntity>>

    @Query("SELECT * FROM dividend_overrides WHERE holdingId = :holdingId")
    suspend fun getByHoldingId(holdingId: String): DividendOverrideEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DividendOverrideEntity)

    @Query("DELETE FROM dividend_overrides WHERE holdingId = :holdingId")
    suspend fun deleteByHoldingId(holdingId: String)
}
