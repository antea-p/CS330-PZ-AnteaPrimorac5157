package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.CreateDiaryEntryRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.impl.DreamDiaryRepositoryImpl
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.EmotionEnum
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.fakes.FakeDreamDiaryApiService

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
    fun `createDiaryEntry creates and returns new entry with title and content`() = runTest {
        // Given
        val title = "New Title"
        val content = "New Content"

        // When
        val result = repository.createDiaryEntry(JWT_TOKEN, title, content, emptyList(), emptyList())

        // Then
        assertEquals(title, result.title)
        assertEquals(content, result.content)
        assertEquals(1, result.id)
    }

    @Test
    fun `createDiaryEntry creates and returns new entry with title, content and tags`() = runTest {
        // Given
        val title = "New Title"
        val content = "New Content"
        val tags = listOf("Stuff")

        // When
        val result = repository.createDiaryEntry(JWT_TOKEN, title, content, emptyList(), tags)

        // Then
        assertEquals(title, result.title)
        assertEquals(content, result.content)
        assertEquals(tags, result.tags)
        assertEquals(1, result.id)
    }

    @Test
    fun `createDiaryEntry creates and returns new entry with title, content and emotions`() = runTest {
        // Given
        val title = "New Title"
        val content = "New Content"
        val emotions = listOf(EmotionEnum.JOY)

        // When
        val result = repository.createDiaryEntry(JWT_TOKEN, title, content, emotions, emptyList())

        // Then
        assertEquals(title, result.title)
        assertEquals(content, result.content)
        assertEquals(listOf("joy"), result.emotions)
        assertEquals(1, result.id)
    }

    @Test
    fun `createDiaryEntry throws exception when API fails`() = runTest {
        // Given
        fakeDreamDiaryApiService.setShouldThrowError(true)

        // When & Then
        assertThrows(Exception::class.java) {
            runTest {
                repository.createDiaryEntry(JWT_TOKEN, "Title", "Content", emptyList(), emptyList())
            }
        }
    }

    @Test
    fun `updateDiaryEntry updates and returns updated entry`() = runTest {
        // Given
        val entry = CreateDiaryEntryRequest("Original Title", "Original Content", emptyList(), emptyList())
        val createdEntry = fakeDreamDiaryApiService.createDiaryEntry(JWT_TOKEN, entry)
        val updatedTitle = "Updated Title"
        val updatedContent = "Updated Content"
        val updatedEmotions = listOf(EmotionEnum.JOY)
        val updatedTags = listOf("UpdatedTag")

        // When
        val result = repository.updateDiaryEntry(JWT_TOKEN, createdEntry.id, updatedTitle, updatedContent, updatedEmotions, updatedTags)

        // Then
        assertEquals(createdEntry.id, result.id)
        assertEquals(updatedTitle, result.title)
        assertEquals(updatedContent, result.content)
        assertEquals(updatedTags, result.tags)
        assertEquals(updatedEmotions.map { it.name.lowercase() }, result.emotions)
    }

    @Test
    fun `updateDiaryEntry throws exception when entry not found`() = runTest {
        // When & Then
        assertThrows(Exception::class.java) {
            runTest {
                repository.updateDiaryEntry(JWT_TOKEN, 999, "Title", "Content", emptyList(), emptyList())
            }
        }
    }

    @Test
    fun `updateDiaryEntry throws exception when API fails`() = runTest {
        // Given
        val entry = CreateDiaryEntryRequest("Original Title", "Original Content", emptyList(), emptyList())
        val createdEntry = fakeDreamDiaryApiService.createDiaryEntry(JWT_TOKEN, entry)
        fakeDreamDiaryApiService.setShouldThrowError(true)

        // When & Then
        assertThrows(Exception::class.java) {
            runTest {
                repository.updateDiaryEntry(JWT_TOKEN, createdEntry.id, "Updated Title", "Updated Content", emptyList(), emptyList())
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