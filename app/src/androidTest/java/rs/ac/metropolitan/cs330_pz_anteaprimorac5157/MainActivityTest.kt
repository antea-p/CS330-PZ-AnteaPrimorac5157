package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.MainActivity

@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun `test navigation to DiaryScreen`() {
        composeTestRule.onNodeWithText("Diary").performClick()
        composeTestRule.onNodeWithText("Diary Screen").assertIsDisplayed()
    }

    @Test
    fun `test navigation to AccountScreen`() {
        composeTestRule.onNodeWithText("Account").performClick()
        composeTestRule.onNodeWithTag("account_screen").assertIsDisplayed()
    }

    @Test
    fun `test bottom navigation bar exists`() {
        composeTestRule.onNodeWithText("Diary").assertIsDisplayed()
        composeTestRule.onNodeWithText("Account").assertIsDisplayed()
    }

    @Test
    fun `test initial screen is DiaryScreen`() {
        composeTestRule.onNodeWithText("Diary Screen").assertIsDisplayed()
    }

    @Test
    fun `test navigation between screens`() {
        composeTestRule.onNodeWithText("Account").performClick()
        composeTestRule.onNodeWithTag("account_screen").assertIsDisplayed()

        composeTestRule.onNodeWithText("Diary").performClick()
        composeTestRule.onNodeWithTag("diary_screen").assertIsDisplayed()
    }
}