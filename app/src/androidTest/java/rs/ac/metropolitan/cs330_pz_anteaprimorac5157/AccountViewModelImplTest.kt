package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.AccountUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.AccountViewModelImpl
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
class AccountViewModelImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var authRepository: AuthenticationRepository

    private lateinit var viewModel: AccountViewModelImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        hiltRule.inject()
        Dispatchers.setMain(testDispatcher)
        viewModel = AccountViewModelImpl(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init checks authentication status`() = runTest {
        // Given
        // Umetnut je FakeAuthenticationRepository

        // When
        viewModel = AccountViewModelImpl(authRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.uiState.value is AccountUiState.LoggedOut)
    }

    @Test
    fun `successful login updates state`() = runTest {
        // When
        viewModel.login("testUser", "testPassword")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.uiState.value is AccountUiState.LoggedIn)
        assert((viewModel.uiState.value as AccountUiState.LoggedIn).username == "testUser")
    }

    @Test
    fun `failed login updates state to error`() = runTest {
        // When
        viewModel.login("wrongUser", "wrongPassword")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.uiState.value is AccountUiState.Error)
    }

    @Test
    fun `logout updates state`() = runTest {
        // Given
        viewModel.login("testUser", "testPassword")
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.uiState.value is AccountUiState.LoggedOut)
    }
}