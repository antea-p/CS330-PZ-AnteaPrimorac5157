package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeCreateEntryViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.EmotionEnum
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens.CreateEntryScreen
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryUiState

@RunWith(AndroidJUnit4::class)
class CreateEntryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: FakeCreateEntryViewModel

    @Before
    fun setUp() {
        viewModel = FakeCreateEntryViewModel()
    }

    @Test
    fun `test submit button is enabled only after user enters non-blank title and content`() {
        composeTestRule.setContent {
            CreateEntryScreen(entryId = null, viewModel = viewModel, onNavigateBack = {})
        }

        composeTestRule.onNodeWithTag("submit_button").assertIsNotEnabled()

        composeTestRule.onNodeWithTag("title_input").performTextInput("Test Title")
        composeTestRule.onNodeWithTag("submit_button").assertIsNotEnabled()

        composeTestRule.onNodeWithTag("title_input").performTextClearance()
        composeTestRule.onNodeWithTag("content_input").performTextInput("Test Content")
        composeTestRule.onNodeWithTag("submit_button").assertIsNotEnabled()

        composeTestRule.onNodeWithTag("title_input").performTextInput("Test Title")
        composeTestRule.onNodeWithTag("submit_button").assertIsEnabled()
    }

    @Test
    fun `test selecting emotion`() {
        composeTestRule.setContent {
            CreateEntryScreen(entryId = null, viewModel = viewModel, onNavigateBack = {})
        }

        composeTestRule.onNodeWithTag("emotion_${EmotionEnum.JOY.name}")
            .performClick()

        // Dodano 5 sekundi cekanja kako bi se osiguralo da assert nece biti izvrsen prerano
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            (viewModel.uiState.value as? CreateEntryUiState.Form)?.emotions?.contains(EmotionEnum.JOY) == true
        }

        val emotions = (viewModel.uiState.value as? CreateEntryUiState.Form)?.emotions

        assertEquals(listOf(EmotionEnum.JOY), emotions)

        composeTestRule.onNodeWithTag("emotion_${EmotionEnum.JOY.name}")
            .assertIsSelected()
    }

    @Test
    fun `test deselecting emotion`() {
        composeTestRule.setContent {
            CreateEntryScreen(entryId = null, viewModel = viewModel, onNavigateBack = {})
        }
        val emotions = (viewModel.uiState.value as? CreateEntryUiState.Form)?.emotions


        composeTestRule.onNodeWithTag("emotion_${EmotionEnum.JOY.name}").performClick()
        composeTestRule.onNodeWithTag("emotion_${EmotionEnum.JOY.name}").performClick()

        assertTrue(emotions!!.isEmpty())
    }

    @Test
    fun `test adding tag`() {
        composeTestRule.setContent {
            CreateEntryScreen(entryId = null, viewModel = viewModel, onNavigateBack = {})
        }
        composeTestRule.onNodeWithTag("tag_input").performTextInput("TestTag")
        composeTestRule.onNodeWithTag("add_tag_button").performClick()
        composeTestRule.onNodeWithText("TestTag").assertIsDisplayed()

        val tags = (viewModel.uiState.value as? CreateEntryUiState.Form)?.tags

        val expectedList = listOf("TestTag")
        assertEquals(expectedList, tags)

    }

    @Test
    fun `test add tag button is enabled only when tag input is not blank`() {
        composeTestRule.setContent {
            CreateEntryScreen(entryId = null, viewModel = viewModel, onNavigateBack = {})
        }

        composeTestRule.onNodeWithTag("add_tag_button").assertIsNotEnabled()
        composeTestRule.onNodeWithTag("tag_input").performTextInput("TestTag")
        composeTestRule.onNodeWithTag("add_tag_button").assertIsEnabled()
    }

    @Test
    fun `test removing tag`() {
        composeTestRule.setContent {
            CreateEntryScreen(entryId = null, viewModel = viewModel, onNavigateBack = {})
        }
        composeTestRule.onNodeWithTag("tag_input").performTextInput("TestTag")
        composeTestRule.onNodeWithTag("add_tag_button").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("remove_tag_TestTag").assertExists().assertHasClickAction().performClick()

        composeTestRule.waitForIdle()

        val tags = (viewModel.uiState.value as? CreateEntryUiState.Form)?.tags

        assertFalse(tags!!.contains("TestTag"))
    }

    @Test
    fun `test submitting a valid form`() {
        composeTestRule.setContent {
            CreateEntryScreen(entryId = null, viewModel = viewModel, onNavigateBack = {})
        }

        composeTestRule.onNodeWithTag("title_input").performTextInput("Test Title")
        composeTestRule.onNodeWithTag("content_input").performTextInput("Test Content")
        composeTestRule.onNodeWithTag("submit_button").performClick()
    }

    @Test
    fun `test editing an existing entry`() {
        val existingEntry = CreateEntryUiState.Form(
            id = 1,
            title = "Existing Title",
            content = "Existing Content",
            emotions = listOf(EmotionEnum.JOY),
            tags = listOf("ExistingTag")
        )
        viewModel.setExistingEntry(existingEntry)

        composeTestRule.setContent {
            CreateEntryScreen(entryId = 1, viewModel = viewModel, onNavigateBack = {})
        }

        composeTestRule.onNodeWithTag("title_input").assertTextContains("Existing Title")
        composeTestRule.onNodeWithTag("content_input").assertTextContains("Existing Content")
        composeTestRule.onNodeWithTag("emotion_JOY").assertIsSelected()
        composeTestRule.onNodeWithText("ExistingTag").assertExists()

        composeTestRule.onNodeWithTag("title_input").performTextReplacement("Updated Title")
        composeTestRule.onNodeWithTag("content_input").performTextReplacement("Updated Content")
        composeTestRule.onNodeWithTag("submit_button").performClick()

        assertEquals("Updated Title", viewModel.lastSubmittedTitle)
        assertEquals("Updated Content", viewModel.lastSubmittedContent)
    }
}