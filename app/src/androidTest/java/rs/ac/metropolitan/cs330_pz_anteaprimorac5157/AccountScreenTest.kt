import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeAccountViewModel
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.screens.AccountScreen

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AccountScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var viewModel: FakeAccountViewModel

    @Before
    fun setUp() {
        viewModel = FakeAccountViewModel()
        composeTestRule.setContent {
            AccountScreen(viewModel = viewModel)
        }
    }

    @Test
    fun `test AccountScreen displays LoginScreen when user is logged out`() {
        composeTestRule.onNodeWithTag("login_screen").assertExists()
    }

    @Test
    fun `test Login button is disabled if username field is blank or empty`() {
        composeTestRule.onNodeWithTag("password_input").performTextInput("testPassword")

        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
    }

    @Test
    fun `test Login button is disabled if password field is blank or empty`() {
        composeTestRule.onNodeWithTag("username_input").performTextInput("testUsername")

        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
    }

    @Test
    fun `test Login button is disabled if both fields are blank or empty`() {
        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
    }

    @Test
    fun `test LoggedInScreen appears after successful login`() {
        composeTestRule.onNodeWithTag("username_input").performTextInput("testUsername")
        composeTestRule.onNodeWithTag("password_input").performTextInput("testPassword")

        composeTestRule.onNodeWithTag("login_button").assertExists()
        composeTestRule.onNodeWithTag("login_button").performClick()

        composeTestRule.onNodeWithTag("logged_in_screen").assertExists()
    }


    @Test
    fun `test LoginScreen appears after user logs out`() {

        composeTestRule.onNodeWithTag("username_input").performTextInput("testUsername")
        composeTestRule.onNodeWithTag("password_input").performTextInput("testPassword")
        composeTestRule.onNodeWithTag("login_button").performClick()

        // Odlogiraj korisnika
        composeTestRule.onNodeWithTag("logout_button").performClick()

        composeTestRule.onNodeWithTag("login_screen").assertExists()

    }
}