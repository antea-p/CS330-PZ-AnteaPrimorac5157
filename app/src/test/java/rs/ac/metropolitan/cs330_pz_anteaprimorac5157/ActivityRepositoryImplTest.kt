package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogEntity
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.impl.ActivityLogRepositoryImpl
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.fakes.FakeActivityLogDao
import java.time.LocalDate

@ExperimentalCoroutinesApi
class ActivityLogRepositoryImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeActivityLogDao: FakeActivityLogDao
    private lateinit var repository: ActivityLogRepositoryImpl

    @Before
    fun setup() {
        fakeActivityLogDao = FakeActivityLogDao()
        repository = ActivityLogRepositoryImpl(fakeActivityLogDao)
    }

    @Test
    fun `insertLog inserts log successfully`() = runTest {
        // Given
        val date = LocalDate.now()

        // When
        repository.insertLog(date)

        // Then
        val insertedLog = fakeActivityLogDao.getInsertedLog()
        assertNotNull(insertedLog)
        assertEquals(date, insertedLog?.date)
    }

    @Test
    fun `getMostRecentLog returns most recent log`() = runTest {
        // Given
        val oldDate = LocalDate.now().minusDays(1)
        val newDate = LocalDate.now()
        fakeActivityLogDao.insert(ActivityLogEntity(date = oldDate))
        fakeActivityLogDao.insert(ActivityLogEntity(date = newDate))

        // When
        val result = repository.getMostRecentLog().first()

        // Then
        assertNotNull(result)
        assertEquals(newDate, result?.date)
    }

    @Test
    fun `getMostRecentLog returns null when no logs exist`() = runTest {
        // When
        val result = repository.getMostRecentLog().first()

        // Then
        assertNull(result)
    }

    @Test
    fun `insertLog throws exception when DAO fails`() = runTest {
        // Given
        fakeActivityLogDao.setShouldThrowError(true)

        // When & Then
        assertThrows(Exception::class.java) {
            runTest {
                repository.insertLog(LocalDate.now())
            }
        }
    }


}