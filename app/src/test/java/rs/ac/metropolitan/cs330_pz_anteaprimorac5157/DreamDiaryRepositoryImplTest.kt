package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.CreateDiaryEntryRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepositoryImpl

@ExperimentalCoroutinesApi
class DreamDiaryRepositoryImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDreamDiaryApiService: FakeDreamDiaryApiService
    private lateinit var repository: DreamDiaryRepositoryImpl

    @Before
    fun setup() {
        fakeDreamDiaryApiService = FakeDreamDiaryApiService()
        repository = DreamDiaryRepositoryImpl(fakeDreamDiaryApiService)
    }

    @Test
    fun `getDiaryEntries returns flow of entries`() = runTest {
        // Given
        val entry1 = CreateDiaryEntryRequest("Title 1", "Content 1", emptyList(), emptyList())
        val entry2 = CreateDiaryEntryRequest("Title 2", "Content 2", emptyList(), emptyList())
        fakeDreamDiaryApiService.createDiaryEntry(entry1)
        fakeDreamDiaryApiService.createDiaryEntry(entry2)

        // When
        val result = repository.getDiaryEntries().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Title 1", result[0].title)
        assertEquals("Title 2", result[1].title)
    }

    @Test
    fun `getDiaryEntries throws exception when API fails`() = runTest {
        // Given
        fakeDreamDiaryApiService.setShouldThrowError(true)

        // When & Then
        assertThrows(Exception::class.java) {
            runTest {
                repository.getDiaryEntries().first()
            }
        }
    }

    @Test
    fun `createDiaryEntry creates and returns new entry`() = runTest {
        // Given
        val title = "New Title"
        val content = "New Content"

        // When
        val result = repository.createDiaryEntry(title, content)

        // Then
        assertEquals(title, result.title)
        assertEquals(content, result.content)
        assertEquals(1, result.id)
    }

    @Test
    fun `createDiaryEntry throws exception when API fails`() = runTest {
        // Given
        fakeDreamDiaryApiService.setShouldThrowError(true)

        // When & Then
        assertThrows(Exception::class.java) {
            runTest {
                repository.createDiaryEntry("Title", "Content")
            }
        }
    }

    @Test
    fun `getDiaryEntryById returns correct entry`() = runTest {
        // Given
        val entry = CreateDiaryEntryRequest("Title", "Content", emptyList(), emptyList())
        fakeDreamDiaryApiService.createDiaryEntry(entry)

        // When
        val result = repository.getDiaryEntryById(1)

        // Then
        assertEquals("Title", result.title)
        assertEquals("Content", result.content)
        assertEquals(1, result.id)
    }

    // TODO
//    @Test
//    fun `getDiaryEntryById throws exception when entry not found`() = runTest {
//        // When & Then
//        assertThrows(NoSuchElementException::class.java) {
//            runTest {
//                repository.getDiaryEntryById(999)
//            }
//        }
//    }

}