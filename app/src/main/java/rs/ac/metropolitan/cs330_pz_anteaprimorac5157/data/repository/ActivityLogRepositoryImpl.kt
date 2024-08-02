package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository

import kotlinx.coroutines.flow.Flow
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogDao
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogEntity
import java.time.LocalDate
import javax.inject.Inject

class ActivityLogRepositoryImpl @Inject constructor(
    private val activityLogDao: ActivityLogDao
) : ActivityLogRepository {

    override suspend fun insertLog(date: LocalDate) {
        activityLogDao.insert(ActivityLogEntity(date = date))
    }

    override fun getMostRecentLog(): Flow<ActivityLogEntity?> {
        return activityLogDao.findMostRecent()
    }
}
