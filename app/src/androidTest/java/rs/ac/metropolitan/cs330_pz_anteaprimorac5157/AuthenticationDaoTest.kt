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
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.AuthenticationDao
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.AuthenticationEntity
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.DreamDiaryDatabase

@RunWith(AndroidJUnit4::class)
class AuthenticationDaoTest {

    private lateinit var db: DreamDiaryDatabase
    private lateinit var dao: AuthenticationDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, DreamDiaryDatabase::class.java).build()
        dao = db.authenticationDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `test login saves AuthenticationEntity`() = runBlocking {
        val auth = AuthenticationEntity(token = "testToken", username = "testUser")
        dao.save(auth)

        val retrieved = dao.find().first()
        assertEquals(auth, retrieved)
    }

    @Test
    fun `test logout deletes AuthenticationEntity`() = runBlocking {
        val auth = AuthenticationEntity(token = "testToken", username = "testUser")
        dao.save(auth)
        dao.delete()

        val retrieved = dao.find().first()
        assertNull(retrieved)
    }

    @Test
    fun `test find when no entry exists`() = runBlocking {
        val retrieved = dao.find().first()
        assertNull(retrieved)
    }

    @Test
    fun `test delete when no entry exists`() = runBlocking {
        dao.delete()
        val retrieved = dao.find().first()
        assertNull(retrieved)
    }
}