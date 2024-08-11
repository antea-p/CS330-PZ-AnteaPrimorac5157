package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeDiaryEntryDetailsViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens.DiaryEntryDetailsScreen

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class DiaryEntryDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: FakeDiaryEntryDetailsViewModel

    @Before
    fun setUp() {
        viewModel = FakeDiaryEntryDetailsViewModel()
    }

    @Test
    fun `test diary entry always has Delete button`() {
        val entry = DiaryEntry(1, "Test Entry", "Content", "2023-01-01", emptyList(), emptyList())
        viewModel.setDiaryEntry(entry)

        composeTestRule.setContent {
            DiaryEntryDetailsScreen(
                entryId = 1,
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToEdit = {}
            )
        }

        composeTestRule.onNodeWithTag("delete_button").assertExists()
    }

    @Test
    fun `test diary entry has no Tag and no Emotion chips if the diary entry has no tags or emotions`() {
        val entry = DiaryEntry(1, "Test Entry", "Content", "2023-01-01", emptyList(), emptyList())
        viewModel.setDiaryEntry(entry)

        composeTestRule.setContent {
            DiaryEntryDetailsScreen(
                entryId = 1,
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToEdit = {}
            )
        }

        composeTestRule.onNodeWithTag("tags_row").assertDoesNotExist()
        composeTestRule.onNodeWithTag("emotions_row").assertDoesNotExist()
    }

    @Test
    fun `test at least one Tag Chip is shown for diary entry details that has at least one tag`() {
        val entry = DiaryEntry(1, "Test Entry", "Content", "2023-01-01", listOf("Tag1"), emptyList())
        viewModel.setDiaryEntry(entry)

        composeTestRule.setContent {
            DiaryEntryDetailsScreen(
                entryId = 1,
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToEdit = {}
            )
        }

        composeTestRule.onNodeWithTag("tags_row").assertExists()
    }

    @Test
    fun `test at least one Emotion Chip is shown for diary entry details that has at least one emotion`() {
        val entry = DiaryEntry(1, "Test Entry", "Content", "2023-01-01", emptyList(), listOf("Happy"))
        viewModel.setDiaryEntry(entry)

        composeTestRule.setContent {
            DiaryEntryDetailsScreen(
                entryId = 1,
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToEdit = {}
            )
        }

        composeTestRule.onNodeWithTag("emotions_row").assertExists()
    }

    @Test
    fun `test Back arrow navigates back to Diary Screen`() {
        var isBackCalled = false
        val entry = DiaryEntry(1, "Test Entry", "Content", "2023-01-01", emptyList(), emptyList())
        viewModel.setDiaryEntry(entry)

        composeTestRule.setContent {
            DiaryEntryDetailsScreen(
                entryId = 1,
                viewModel = viewModel,
                onNavigateBack = { isBackCalled = true },
                onNavigateToEdit = {}
            )
        }

        composeTestRule.onNodeWithTag("back_button").performClick()
        assert(isBackCalled)
    }

    @Test
    fun `test Edit button navigates to Edit Screen`() {
        var navigatedToEditId: Int? = null
        val entry = DiaryEntry(1, "Test Entry", "Content", "2023-01-01", emptyList(), emptyList())
        viewModel.setDiaryEntry(entry)

        composeTestRule.setContent {
            DiaryEntryDetailsScreen(
                entryId = 1,
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToEdit = { id -> navigatedToEditId = id }
            )
        }

        composeTestRule.onNodeWithTag("edit_button").performClick()
        assert(navigatedToEditId == 1)
    }

    @Test
    fun `test Delete button shows confirmation dialog`() {
        val entry = DiaryEntry(1, "Test Entry", "Content", "2023-01-01", emptyList(), emptyList())
        viewModel.setDiaryEntry(entry)

        composeTestRule.setContent {
            DiaryEntryDetailsScreen(
                entryId = 1,
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToEdit = {}
            )
        }

        composeTestRule.onNodeWithTag("delete_button").assertExists()
        composeTestRule.onNodeWithTag("delete_button").performClick()

        // Provjeri da se otvara dijalog potvrde
        composeTestRule.onNodeWithText("Delete Entry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Are you sure you want to delete this entry? This action cannot be undone.").assertIsDisplayed()

        // Provjeri da postoje i dugme za potvrdu i za ponistavanje brisanja
        composeTestRule.onNodeWithTag("confirm_delete_button").assertExists()
        composeTestRule.onNodeWithTag("dismiss_delete_button").assertExists()

    }

}