package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import org.junit.Test
import org.junit.Assert.*
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.navigation.NavigationRoutes

class NavigationRoutesTest {

    @Test
    fun testDiaryRoute() {
        assertEquals("diary", NavigationRoutes.Diary.route)
    }

    @Test
    fun testAccountRoute() {
        assertEquals("account", NavigationRoutes.Account.route)
    }
}