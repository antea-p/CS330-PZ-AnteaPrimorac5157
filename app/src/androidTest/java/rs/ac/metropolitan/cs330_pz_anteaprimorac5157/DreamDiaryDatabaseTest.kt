package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.DreamDiaryDatabase

@RunWith(AndroidJUnit4::class)
class DreamDiaryDatabaseTest {

    private lateinit var db: DreamDiaryDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DreamDiaryDatabase::class.java).build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun daoAreNotNull() {
        assertNotNull(db.authenticationRepository())
        assertNotNull(db.activityLogRepository())
    }
}