package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogDao
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogEntity
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.DreamDiaryDatabase
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class ActivityLogDaoTest {

    private lateinit var db: DreamDiaryDatabase
    private lateinit var dao: ActivityLogDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DreamDiaryDatabase::class.java).build()
        dao = db.activityLogRepository()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndFindMostRecent() = runBlocking {
        val activityLog = ActivityLogEntity(date = LocalDate.now())
        dao.insert(activityLog)

        val retrieved = dao.findMostRecent().first()
        assertEquals(activityLog.date, retrieved?.date)
    }

    @Test
    fun findMostRecentWhenEmpty() = runBlocking {
        val retrieved = dao.findMostRecent().first()
        assertNull(retrieved)
    }
}