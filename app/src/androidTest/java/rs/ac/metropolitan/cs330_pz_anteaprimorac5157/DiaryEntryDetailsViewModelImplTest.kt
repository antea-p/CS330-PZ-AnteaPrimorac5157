package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeAuthenticationService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeDreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryEntryDetailsUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.DiaryEntryDetailsViewModelImpl

@ExperimentalCoroutinesApi
class DiaryEntryDetailsViewModelImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: DiaryEntryDetailsViewModelImpl
    private lateinit var fakeDreamDiaryRepository: FakeDreamDiaryRepository
    private lateinit var fakeAuthService: FakeAuthenticationService
    private var JWT_TOKEN: String = "fake-token"

    @Before
    fun setup() {
        fakeDreamDiaryRepository = FakeDreamDiaryRepository()
        fakeAuthService = FakeAuthenticationService()
        viewModel = DiaryEntryDetailsViewModelImpl(fakeDreamDiaryRepository, fakeAuthService)
    }

    @Test
    fun `loadDiaryEntry loads correct entry`() = runTest {
        // Given
        val createdEntry = fakeDreamDiaryRepository.createDiaryEntry(JWT_TOKEN, "Test Title", "Test Content", listOf(), listOf())

        // When
        viewModel.loadDiaryEntry(createdEntry.id)

        // Then
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DiaryEntryDetailsUiState.Success)
        assertEquals(createdEntry, (state as DiaryEntryDetailsUiState.Success).entry)
    }

    @Test
    fun `loadDiaryEntry sets error state when entry not found`() = runTest {
        // When
        viewModel.loadDiaryEntry(999)

        // Then
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DiaryEntryDetailsUiState.Error)
    }

    @Test
    fun `deleteDiaryEntry deletes entry and sets Deleted state`() = runTest {
        // Given
        val createdEntry = fakeDreamDiaryRepository.createDiaryEntry(JWT_TOKEN, "Test Title", "Test Content", listOf(), listOf())

        // When
        viewModel.deleteDiaryEntry(createdEntry.id)

        // Then
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DiaryEntryDetailsUiState.Deleted)
        assertThrows(NoSuchElementException::class.java) {
            runBlocking { fakeDreamDiaryRepository.getDiaryEntryById(JWT_TOKEN, createdEntry.id) }
        }
    }

    @Test
    fun `deleteDiaryEntry sets error state when entry not found`() = runTest {
        // When
        viewModel.deleteDiaryEntry(999)

        // Then
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DiaryEntryDetailsUiState.Error)
    }
}