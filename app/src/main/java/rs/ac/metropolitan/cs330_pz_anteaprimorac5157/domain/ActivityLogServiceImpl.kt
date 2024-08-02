package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.ActivityLogRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class ActivityLogServiceImpl @Inject constructor(
    private val repository: ActivityLogRepository
) : ActivityLogService {

    override suspend fun logCurrentDate() {
        val today = LocalDate.now()
        repository.insertLog(today)
    }

    override fun getLastLoggedDate(): Flow<String?> {
        return repository.getMostRecentLog().map { logEntity ->
            logEntity?.let { calculateDaysAgo(it.date) }
        }
    }

    private fun calculateDaysAgo(date: LocalDate): String {
        val daysAgo = ChronoUnit.DAYS.between(date, LocalDate.now()).toInt()
        return when (daysAgo) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> "$daysAgo days ago"
        }
    }
}
