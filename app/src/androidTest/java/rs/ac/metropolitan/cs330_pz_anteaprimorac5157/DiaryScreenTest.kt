package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import FakeDiaryViewModel
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.DiaryScreen

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DiaryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `test DiaryScreen shows logged out message when state is LoggedOut`() {
        val viewModel = FakeDiaryViewModel(loggedIn = false)

        composeTestRule.setContent {
            DiaryScreen(
                viewModel = viewModel,
                onCreateEntry = {},
                onEntryClick = {},
                needsRefresh = false,
                onRefreshComplete = {}
            )
        }

        composeTestRule.onNodeWithTag("logged_out_screen").assertIsDisplayed()
        composeTestRule.onNodeWithText("You are not currently logged in. Go to the account tab to log in!").assertIsDisplayed()
    }

    @Test
    fun `test DiaryScreen shows diary entries when state is LoggedIn and at least one diary entry exists`() {
        val viewModel = FakeDiaryViewModel(loggedIn = true)
        viewModel.loadDiaryEntries()

        composeTestRule.setContent {
            DiaryScreen(
                viewModel = viewModel,
                onCreateEntry = {},
                onEntryClick = {},
                needsRefresh = false,
                onRefreshComplete = {}
            )
        }

        composeTestRule.onNodeWithTag("diary_entries_list").assertIsDisplayed()
        composeTestRule.onNodeWithText("First Entry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second Entry").assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("diary_entry_item").assertCountEquals(2)
    }


    @Test
    fun `test DiaryScreen shows empty diary entries list when state is LoggedIn but no diary entries exist`() {
        val viewModel = FakeDiaryViewModel(loggedIn = true)
        viewModel.loadEmptyDiaryEntryList()

        composeTestRule.setContent {
            DiaryScreen(
                viewModel = viewModel,
                onCreateEntry = {},
                onEntryClick = {},
                needsRefresh = false,
                onRefreshComplete = {}
            )
        }

        composeTestRule.onNodeWithTag("empty_diary_entries_list").assertIsDisplayed()
        composeTestRule.onNodeWithText("Your diary entry list is currently empty.").assertIsDisplayed()
    }

    @Test
    fun `test when diary entry clicked retrieves correct ID`() {
        val viewModel = FakeDiaryViewModel(loggedIn = true)
        var navigatedToId: Int? = null

        viewModel.loadDiaryEntries()

        composeTestRule.setContent {
            DiaryScreen(
                viewModel = viewModel,
                onCreateEntry = {},
                onEntryClick = { id -> navigatedToId = id },
                needsRefresh = false,
                onRefreshComplete = {}
            )
        }

        composeTestRule.onNodeWithText("First Entry").performClick()
        assertEquals(1, navigatedToId)
    }

    @Test
    fun `test when Create button clicked`() {
        val viewModel = FakeDiaryViewModel(loggedIn = true)
        var navigatedToCreateDiaryEntry = false

        viewModel.loadDiaryEntries()

        composeTestRule.setContent {
            DiaryScreen(
                viewModel = viewModel,
                onCreateEntry = { navigatedToCreateDiaryEntry = true },
                onEntryClick = {},
                needsRefresh = false,
                onRefreshComplete = {}
            )
        }

        composeTestRule.onNodeWithTag("create_diary_entry_btn").performClick()
        assertEquals(true, navigatedToCreateDiaryEntry)
    }
}