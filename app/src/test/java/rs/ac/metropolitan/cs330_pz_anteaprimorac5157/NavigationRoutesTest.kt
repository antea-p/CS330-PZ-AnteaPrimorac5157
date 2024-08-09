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
        assertEquals("diary_entry_details/{entryId}", NavigationRoutes.DiaryEntryDetails.route)
    }

    @Test
    fun `test Create Entry route gets valid ID when editing existing entry`() {
        assertEquals("create_entry?entryId={entryId}", NavigationRoutes.CreateEntry.route)
    }

    @Test
    fun `test Create Entry route creates valid route for new entry`() {
        assertEquals("create_entry", NavigationRoutes.CreateEntry.createRoute())
        assertEquals("create_entry?entryId=1", NavigationRoutes.CreateEntry.createRoute(1))
    }

    @Test
    fun `test Diary Entry Details route creation`() {
        assertEquals("diary_entry_details/1", NavigationRoutes.DiaryEntryDetails.createRoute(1))
    }

    @Test
    fun `test route uniqueness`() {
        val routes = listOf(NavigationRoutes.Diary.route, NavigationRoutes.Account.route)
        assertEquals(routes.size, routes.distinct().size)
    }
}