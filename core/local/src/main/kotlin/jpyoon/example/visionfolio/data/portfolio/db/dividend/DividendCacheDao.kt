package jpyoon.example.visionfolio.data.portfolio.db.dividend

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DividendCacheDao {

    @Query("SELECT * FROM dividend_cache WHERE holdingId = :holdingId")
    suspend fun getByHoldingId(holdingId: String): List<DividendCacheEntity>

    @Query("SELECT cachedAt FROM dividend_cache WHERE holdingId = :holdingId LIMIT 1")
    suspend fun getCachedAt(holdingId: String): Long?

    @Query("SELECT quantity FROM dividend_cache WHERE holdingId = :holdingId LIMIT 1")
    suspend fun getCachedQuantity(holdingId: String): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<DividendCacheEntity>)

    @Query("DELETE FROM dividend_cache WHERE holdingId = :holdingId")
    suspend fun deleteByHoldingId(holdingId: String)

    @Query("DELETE FROM dividend_cache")
    suspend fun clearAll()
}
