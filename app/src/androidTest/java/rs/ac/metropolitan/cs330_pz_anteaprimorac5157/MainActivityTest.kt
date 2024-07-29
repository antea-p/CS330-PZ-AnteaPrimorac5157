package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.MainActivity

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
class MainActivityTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNavigationToDiaryScreen() {
        composeTestRule.onNodeWithText("Diary").performClick()
        composeTestRule.onNodeWithText("Diary Screen").assertIsDisplayed()
    }

    @Test
    fun testNavigationToAccountScreen() {
        composeTestRule.onNodeWithText("Account").performClick()
        composeTestRule.onNodeWithText("Account Screen").assertIsDisplayed()
    }

    @Test
    fun testBottomNavigationBarExists() {
        composeTestRule.onNodeWithText("Diary").assertIsDisplayed()
        composeTestRule.onNodeWithText("Account").assertIsDisplayed()
    }

    @Test
    fun testInitialScreenIsDiary() {
        composeTestRule.onNodeWithText("Diary Screen").assertIsDisplayed()
    }

    @Test
    fun testNavigationBetweenScreens() {
        composeTestRule.onNodeWithText("Account").performClick()
        composeTestRule.onNodeWithText("Account Screen").assertIsDisplayed()

        composeTestRule.onNodeWithText("Diary").performClick()
        composeTestRule.onNodeWithText("Diary Screen").assertIsDisplayed()
    }
}