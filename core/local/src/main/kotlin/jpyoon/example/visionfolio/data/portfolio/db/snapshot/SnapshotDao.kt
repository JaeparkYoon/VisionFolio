package jpyoon.example.visionfolio.data.portfolio.db.snapshot

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SnapshotDao {

    @Query("SELECT * FROM portfolio_snapshots ORDER BY timestamp ASC")
    fun observeAll(): Flow<List<SnapshotEntity>>

    @Query("SELECT * FROM portfolio_snapshots WHERE timestamp BETWEEN :from AND :to ORDER BY timestamp ASC")
    fun observeRange(from: Long, to: Long): Flow<List<SnapshotEntity>>

    @Query("SELECT * FROM portfolio_snapshots ORDER BY timestamp DESC LIMIT 1")
    suspend fun latest(): SnapshotEntity?

    @Query("SELECT MIN(timestamp) FROM portfolio_snapshots")
    fun observeOldestTimestamp(): Flow<Long?>

    @Query("SELECT COUNT(*) FROM portfolio_snapshots WHERE timestamp = :timestamp")
    suspend fun exists(timestamp: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SnapshotEntity)

    @Query("DELETE FROM portfolio_snapshots")
    suspend fun clear()
}
