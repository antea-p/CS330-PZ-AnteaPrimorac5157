package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.DreamDiaryDatabase
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
class DreamDiaryDatabaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var db: DreamDiaryDatabase

    @Before
    fun initDb() {
        hiltRule.inject()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `dao are not null`() {
        assertNotNull(db.authenticationDao())
        assertNotNull(db.activityLogDao())
    }
}