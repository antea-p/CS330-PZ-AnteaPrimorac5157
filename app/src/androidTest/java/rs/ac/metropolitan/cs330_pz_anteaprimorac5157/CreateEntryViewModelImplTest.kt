package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import kotlinx.coroutines.flow.first

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeAuthenticationService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di.FakeDreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.EmotionEnum
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.CreateEntryUiState
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.ui.viewmodel.impl.CreateEntryViewModelImpl

@ExperimentalCoroutinesApi
class CreateEntryViewModelImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CreateEntryViewModelImpl
    private lateinit var fakeDreamDiaryRepository: FakeDreamDiaryRepository
    private lateinit var fakeAuthService: FakeAuthenticationService
    private var JWT_TOKEN: String = "fake-token"

    @Before
    fun setup() {
        fakeDreamDiaryRepository = FakeDreamDiaryRepository()
        fakeAuthService = FakeAuthenticationService()
        runBlocking {
            fakeAuthService.login("testUser", "testPassword")
        }
        viewModel = CreateEntryViewModelImpl(fakeDreamDiaryRepository, fakeAuthService)
    }

    @Test
    fun `createDiaryEntry with title and content`() = runTest {
        // Given
        viewModel.onTitleChanged("Test Title")
        viewModel.onContentChanged("Test Content")

        // When
        viewModel.onSubmit()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue("State should be Success, but was $state", state is CreateEntryUiState.Success)

        val entries = fakeDreamDiaryRepository.getDiaryEntries(JWT_TOKEN).first()
        assertEquals(1, entries.size)
        assertEquals("Test Title", entries[0].title)
        assertEquals("Test Content", entries[0].content)
    }

    @Test
    fun `createDiaryEntry with title content and tags`() = runTest {
        // Given
        viewModel.onTitleChanged("Test Title")
        viewModel.onContentChanged("Test Content")
        viewModel.onTagAdded("Tag1")
        viewModel.onTagAdded("Tag2")

        // When
        viewModel.onSubmit()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is CreateEntryUiState.Success)
        val entries = fakeDreamDiaryRepository.getDiaryEntries(JWT_TOKEN).first()
        assertEquals(1, entries.size)
        assertEquals("Test Title", entries[0].title)
        assertEquals("Test Content", entries[0].content)
        assertEquals(listOf("Tag1", "Tag2"), entries[0].tags)
    }

    @Test
    fun `createDiaryEntry with title content and emotions`() = runTest {
        // Given
        viewModel.onTitleChanged("Test Title")
        viewModel.onContentChanged("Test Content")
        viewModel.onEmotionChanged(EmotionEnum.JOY)
        viewModel.onEmotionChanged(EmotionEnum.CURIOSITY)

        // When
        viewModel.onSubmit()
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is CreateEntryUiState.Success)
        val entries = fakeDreamDiaryRepository.getDiaryEntries(JWT_TOKEN).first()
        assertEquals(1, entries.size)
        assertEquals("Test Title", entries[0].title)
        assertEquals("Test Content", entries[0].content)
        assertEquals(listOf("JOY", "CURIOSITY"), entries[0].emotions)
    }
}