package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain

import kotlinx.coroutines.flow.Flow

interface ActivityLogService {
    suspend fun logCurrentDate()
    fun getLastLoggedDate(): Flow<String?>
}
