package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import androidx.compose.foundation.layout.PaddingValues
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.CreateDiaryEntryRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepositoryImpl

@ExperimentalCoroutinesApi
class DreamDiaryRepositoryImplTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var fakeDreamDiaryApiService: FakeDreamDiaryApiService
    private lateinit var repository: DreamDiaryRepositoryImpl
    private var JWT_TOKEN: String = "Bearer fake-token"

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
        fakeDreamDiaryApiService.createDiaryEntry(JWT_TOKEN, entry1)
        fakeDreamDiaryApiService.createDiaryEntry(JWT_TOKEN, entry2)

        // When
        val result = repository.getDiaryEntries(JWT_TOKEN).first()

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
                repository.getDiaryEntries(JWT_TOKEN).first()
            }
        }
    }

    @Test
    fun `createDiaryEntry creates and returns new entry`() = runTest {
        // Given
        val title = "New Title"
        val content = "New Content"

        // When
        val result = repository.createDiaryEntry(JWT_TOKEN, title, content)

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
                repository.createDiaryEntry(JWT_TOKEN, "Title", "Content")
            }
        }
    }

    @Test
    fun `getDiaryEntryById returns correct entry`() = runTest {
        // Given
        val entry = CreateDiaryEntryRequest("Title", "Content", emptyList(), emptyList())
        fakeDreamDiaryApiService.createDiaryEntry(JWT_TOKEN, entry)

        // When
        val result = repository.getDiaryEntryById(JWT_TOKEN, 1)

        // Then
        assertEquals("Title", result.title)
        assertEquals("Content", result.content)
        assertEquals(1, result.id)
    }

    @Test
    fun `getDiaryEntryById throws Exception in case of other exceptions`() = runTest {
        // When & Then
        fakeDreamDiaryApiService.setShouldThrowError(true)
        assertThrows(Exception::class.java) {
            runTest {
                repository.getDiaryEntryById(JWT_TOKEN, 999)
            }
        }
    }

    @Test
    fun `deleteDiaryEntry deletes entry successfully`() = runTest {
        // Given
        val entry = CreateDiaryEntryRequest("Title", "Content", emptyList(), emptyList())
        val createdEntry = fakeDreamDiaryApiService.createDiaryEntry(JWT_TOKEN, entry)

        // When
        repository.deleteDiaryEntry(JWT_TOKEN, createdEntry.id)

        // Then
        assertThrows(Exception::class.java) {
            runTest {
                repository.getDiaryEntryById(JWT_TOKEN, createdEntry.id)
            }
        }
    }

    @Test
    fun `deleteDiaryEntry throws exception when entry not found`() = runTest {
        // When & Then
        fakeDreamDiaryApiService.setShouldThrowError(true)
        assertThrows(Exception::class.java) {
            runTest {
                repository.deleteDiaryEntry(JWT_TOKEN, 999)
            }
        }
    }


}