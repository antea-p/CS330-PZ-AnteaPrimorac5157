package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import org.junit.Test
import org.junit.Assert.*
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.navigation.NavigationRoutes

class NavigationRoutesTest {

    @Test
    fun `test Diary route`() {
        assertEquals("diary", NavigationRoutes.Diary.route)
    }

    @Test
    fun `test Account route`() {
        assertEquals("account", NavigationRoutes.Account.route)
    }

    @Test
    fun `test Diary Entry Details route`() {
        assertEquals("diary_entry_details", NavigationRoutes.DiaryEntryDetails.route)
    }

    @Test
    fun `test route uniqueness`() {
        val routes = listOf(NavigationRoutes.Diary.route, NavigationRoutes.Account.route)
        assertEquals(routes.size, routes.distinct().size)
    }
}