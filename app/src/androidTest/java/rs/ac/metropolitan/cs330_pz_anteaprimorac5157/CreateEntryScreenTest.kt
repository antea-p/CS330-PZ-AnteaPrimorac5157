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
            CreateEntryScreen(viewModel = viewModel, onNavigateBack = {})
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
            CreateEntryScreen(viewModel = viewModel, onNavigateBack = {})
        }
        composeTestRule.onNodeWithTag("emotion_${EmotionEnum.JOY.name}").performClick()
        //composeTestRule.onNodeWithTag("emotion_${EmotionEnum.JOY.name}").assertIsSelected()

        val emotions = (viewModel.uiState.value as? CreateEntryUiState.Form)?.emotions

        val expectedList = listOf(EmotionEnum.JOY)
        assertEquals(expectedList, emotions)
    }

    @Test
    fun `test deselecting emotion`() {
        composeTestRule.setContent {
            CreateEntryScreen(viewModel = viewModel, onNavigateBack = {})
        }
        val emotions = (viewModel.uiState.value as? CreateEntryUiState.Form)?.emotions


        composeTestRule.onNodeWithTag("emotion_${EmotionEnum.JOY.name}").performClick()
        composeTestRule.onNodeWithTag("emotion_${EmotionEnum.JOY.name}").performClick()

        assertTrue(emotions!!.isEmpty())
    }

    @Test
    fun `test adding tag`() {
        composeTestRule.setContent {
            CreateEntryScreen(viewModel = viewModel, onNavigateBack = {})
        }
        composeTestRule.onNodeWithTag("tag_input").performTextInput("TestTag")
        composeTestRule.onNodeWithTag("add_tag_button").performClick()
        composeTestRule.onNodeWithText("TestTag").assertIsDisplayed()

        val tags = (viewModel.uiState.value as? CreateEntryUiState.Form)?.tags

        val expectedList = listOf("TestTag")
        assertEquals(expectedList, tags)

    }

    @Test
    fun `test removing tag`() {
        composeTestRule.setContent {
            CreateEntryScreen(viewModel = viewModel, onNavigateBack = {})
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
            CreateEntryScreen(viewModel = viewModel, onNavigateBack = {})
        }

        composeTestRule.onNodeWithTag("title_input").performTextInput("Test Title")
        composeTestRule.onNodeWithTag("content_input").performTextInput("Test Content")
        composeTestRule.onNodeWithTag("submit_button").performClick()
        assert(viewModel.uiState.value is CreateEntryUiState.Success)
    }
}