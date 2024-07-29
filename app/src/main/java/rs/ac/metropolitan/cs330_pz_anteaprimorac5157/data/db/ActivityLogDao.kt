package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Insert
    suspend fun insert(activityLogEntity: ActivityLogEntity)

    @Query("SELECT * FROM activity_log ORDER BY date DESC LIMIT 1")
    fun findMostRecent(): Flow<ActivityLogEntity?>
}