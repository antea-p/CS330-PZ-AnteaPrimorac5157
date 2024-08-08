package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeActivityLogService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeAuthenticationService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeDreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl.DiaryViewModelImpl

@ExperimentalCoroutinesApi
class DiaryViewModelImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DiaryViewModelImpl
    private lateinit var fakeDiaryRepository: FakeDreamDiaryRepository
    private lateinit var fakeAuthService: FakeAuthenticationService
    private lateinit var fakeActivityLogService: FakeActivityLogService
    private val testDispatcher = StandardTestDispatcher()
    private var JWT_TOKEN: String = "fake-token"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeDiaryRepository = FakeDreamDiaryRepository()
        fakeAuthService = FakeAuthenticationService()
        fakeActivityLogService = FakeActivityLogService()
        viewModel = DiaryViewModelImpl(fakeDiaryRepository, fakeAuthService, fakeActivityLogService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init checks authentication status and loads entries if logged in`() = runTest {
        // Given
        fakeAuthService.login("testUser", "testPassword")

        // When
        viewModel = DiaryViewModelImpl(fakeDiaryRepository, fakeAuthService, fakeActivityLogService)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.uiState.value is DiaryUiState.Success)
    }

    @Test
    fun `init sets LoggedOut state if user is not authenticated`() = runTest {
        // Given
        fakeAuthService.logout()

        // When
        viewModel = DiaryViewModelImpl(fakeDiaryRepository, fakeAuthService, fakeActivityLogService)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(viewModel.uiState.value is DiaryUiState.LoggedOut)
    }

    @Test
    fun `loadDiaryEntries updates state to Success when entries are loaded`() = runTest {
        // Given
        fakeAuthService.login("testUser", "testPassword")
        fakeDiaryRepository.createDiaryEntry(JWT_TOKEN, "Test Title", "Test Content", listOf(), listOf())

        // When
        viewModel.loadDiaryEntries()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assert(state is DiaryUiState.Success)
        assert((state as DiaryUiState.Success).entries.size == 1)
        assert(state.entries[0].title == "Test Title")
    }

    @Test
    fun `logAppOpen is called when user is logged in`() = runTest {
        // Given
        fakeAuthService.login("testUser", "testPassword")

        // When
        viewModel = DiaryViewModelImpl(fakeDiaryRepository, fakeAuthService, fakeActivityLogService)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(fakeActivityLogService.logCurrentDateCalled)
    }

    @Test
    fun `logAppOpen is not called when user is logged out`() = runTest {
        // Given
        fakeAuthService.logout()

        // When
        viewModel = DiaryViewModelImpl(fakeDiaryRepository, fakeAuthService, fakeActivityLogService)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assert(!fakeActivityLogService.logCurrentDateCalled)
    }

    @Test
    fun `loadDiaryEntries still succeeds when logAppOpen fails`() = runTest {
        // Given
        fakeAuthService.login("testUser", "testPassword")
        fakeActivityLogService.shouldThrowException = true
        fakeDiaryRepository.createDiaryEntry(JWT_TOKEN, "Test Title", "Test Content", listOf(), listOf())

        // When
        viewModel = DiaryViewModelImpl(fakeDiaryRepository, fakeAuthService, fakeActivityLogService)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assert(state is DiaryUiState.Success)
        assert((state as DiaryUiState.Success).entries.size == 1)
    }
}
