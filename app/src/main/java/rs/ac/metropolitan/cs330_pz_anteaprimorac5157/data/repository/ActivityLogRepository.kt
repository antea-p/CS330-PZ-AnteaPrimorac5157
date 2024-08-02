package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository

import kotlinx.coroutines.flow.Flow
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogEntity
import java.time.LocalDate

interface ActivityLogRepository {
    suspend fun insertLog(date: LocalDate)
    fun getMostRecentLog(): Flow<ActivityLogEntity?>
}
