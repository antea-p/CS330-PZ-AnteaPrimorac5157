package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.fakes

import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.ActivityLogRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogEntity
import java.time.LocalDate

class FakeActivityLogRepository : ActivityLogRepository {
    private var mostRecentLog: ActivityLogEntity? = null

    override suspend fun insertLog(date: LocalDate) {
        mostRecentLog = ActivityLogEntity(date = date)
    }

    override fun getMostRecentLog(): Flow<ActivityLogEntity?> = flowOf(mostRecentLog)
}