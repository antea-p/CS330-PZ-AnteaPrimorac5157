package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.ActivityLogService
import java.time.LocalDate

class FakeActivityLogService : ActivityLogService {
    private var lastLoggedDate: LocalDate? = null
    var logCurrentDateCalled = false
    var shouldThrowException = false

    override suspend fun logCurrentDate() {
        if (shouldThrowException) {
            throw Exception("Failed to log current date")
        }
        lastLoggedDate = LocalDate.now()
        logCurrentDateCalled = true
    }

    override fun getLastLoggedDate(): Flow<String?> {
        return flowOf(lastLoggedDate?.toString())
    }
}
