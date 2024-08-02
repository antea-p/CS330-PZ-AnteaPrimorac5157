package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.ActivityLogServiceImpl
import java.time.LocalDate

@ExperimentalCoroutinesApi
class ActivityLogServiceImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: ActivityLogServiceImpl
    private lateinit var fakeRepository: FakeActivityLogRepository

    @Before
    fun setup() {
        fakeRepository = FakeActivityLogRepository()
        service = ActivityLogServiceImpl(fakeRepository)
    }

    @Test
    fun `logCurrentDate inserts current date into repository`() = runTest {
        service.logCurrentDate()
        val result = fakeRepository.getMostRecentLog().first()
        assert(result?.date == LocalDate.now())
    }

    @Test
    fun `getLastLoggedDate returns correct string representation`() = runTest {
        val today = LocalDate.now()
        fakeRepository.insertLog(today)

        val result = service.getLastLoggedDate().first()
        assert(result == "Today")

        fakeRepository.insertLog(today.minusDays(1))
        val yesterdayResult = service.getLastLoggedDate().first()
        assert(yesterdayResult == "Yesterday")

        fakeRepository.insertLog(today.minusDays(5))
        val daysAgoResult = service.getLastLoggedDate().first()
        assert(daysAgoResult == "5 days ago")
    }
}