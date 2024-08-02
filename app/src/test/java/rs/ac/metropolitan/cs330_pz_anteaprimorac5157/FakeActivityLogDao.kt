package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogDao
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogEntity

class FakeActivityLogDao : ActivityLogDao {
    private val logs = mutableListOf<ActivityLogEntity>()
    private var shouldThrowError = false

    override suspend fun insert(activityLogEntity: ActivityLogEntity) {
        if (shouldThrowError) {
            throw Exception("Simulated DAO error")
        }
        logs.add(activityLogEntity)
    }

    override fun findMostRecent() = kotlinx.coroutines.flow.flow {
        emit(logs.maxByOrNull { it.date })
    }

    fun getInsertedLog() = logs.lastOrNull()

    fun setShouldThrowError(value: Boolean) {
        shouldThrowError = value
    }
}