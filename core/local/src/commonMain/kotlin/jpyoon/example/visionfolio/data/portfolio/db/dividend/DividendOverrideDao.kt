package jpyoon.example.visionfolio.data.portfolio.db.dividend

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DividendOverrideDao {

    @Query("SELECT * FROM dividend_override WHERE holdingId = :holdingId")
    suspend fun getByHoldingId(holdingId: String): DividendOverrideEntity?

    @Query("SELECT * FROM dividend_override")
    fun observeAll(): Flow<List<DividendOverrideEntity>>

    @Upsert
    suspend fun upsert(entity: DividendOverrideEntity)

    @Query("DELETE FROM dividend_override WHERE holdingId = :holdingId")
    suspend fun deleteByHoldingId(holdingId: String)
}
