package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.CreateDiaryEntryRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DreamDiaryApiService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.Emotion
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.Tag
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.UpdateDiaryEntryRequest

class ApiClientTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: DreamDiaryApiService
    private var JWT_TOKEN: String = "fake-token"

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        val gson = GsonBuilder()
            .setLenient()
            .create()

        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DreamDiaryApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `login returns token on successful request`() = runBlocking {
        // Given
        val expectedToken = "fake-jwt-token"
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "token": "$JWT_TOKEN",
                    "message": "Login successful"
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val response = api.login(LoginRequest("omori", "password"))

        // Then
        assertEquals(JWT_TOKEN, response.token)
        assertEquals("Login successful", response.message)

        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/api/v1/auth/login", request.path)
        val requestBody = request.body.readUtf8()
        assertTrue(requestBody.contains("\"username\":\"omori\""))
        assertTrue(requestBody.contains("\"password\":\"password\""))
    }

    @Test
    fun `checkAuthentication is called correctly with Authentication header`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "authenticated": true
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val response = api.checkAuthentication("Bearer $JWT_TOKEN")

        // Then
        assertTrue(response.authenticated)

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/api/v1/auth/check", request.path)
        assertEquals("Bearer $JWT_TOKEN", request.getHeader("Authorization"))
    }

    @Test
    fun `getDiaryEntries returns list of diary entries`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                [
                    {
                        "id": 1,
                        "title": "Test Entry",
                        "content": "This is a test entry",
                        "createdDate": "2024-05-01",
                        "userId": 1,
                        "tags": [{"id": 1, "name": "Test"}],
                        "emotions": [{"id": 1, "name": "Happy"}]
                    }
                ]
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val entries = api.getDiaryEntries("Bearer $JWT_TOKEN")

        // Then
        assertEquals(1, entries.size)
        assertEquals("Test Entry", entries[0].title)
        assertEquals("This is a test entry", entries[0].content)
        assertEquals(1, entries[0].tags.size)
        assertEquals(1, entries[0].emotions.size)

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/api/v1/diary", request.path)
        assertEquals("Bearer $JWT_TOKEN", request.getHeader("Authorization"))
    }

    @Test
    fun `getDiaryEntryById returns a single diary entry`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "id": 1,
                    "title": "Test Entry",
                    "content": "This is a test entry",
                    "createdDate": "2024-05-01",
                    "userId": 1,
                    "tags": [{"id": 1, "name": "Test"}],
                    "emotions": [{"id": 1, "name": "Happy"}]
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val entry = api.getDiaryEntryById("Bearer $JWT_TOKEN", 1)

        // Then
        assertEquals(1, entry.id)
        assertEquals("Test Entry", entry.title)
        assertEquals("This is a test entry", entry.content)

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/api/v1/diary/1", request.path)
        assertEquals("Bearer $JWT_TOKEN", request.getHeader("Authorization"))
    }

    @Test
    fun `createDiaryEntry creates a new diary entry - no tags or emotions`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(201)
            .setBody("""
                {
                    "id": 2,
                    "title": "New Entry",
                    "content": "This is a new entry",
                    "createdDate": "2024-05-02",
                    "userId": 1,
                    "tags": [],
                    "emotions": []
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val newEntry = api.createDiaryEntry("Bearer $JWT_TOKEN", CreateDiaryEntryRequest(
            "New Entry",
            "This is a new entry",
            emptyList(),
            emptyList()
        ))

        // Then
        assertEquals(2, newEntry.id)
        assertEquals("New Entry", newEntry.title)
        assertEquals("This is a new entry", newEntry.content)

        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/api/v1/diary", request.path)
        assertEquals("Bearer $JWT_TOKEN", request.getHeader("Authorization"))

        val requestBody = request.body.readUtf8()
        assertTrue(requestBody.contains("\"title\":\"New Entry\""))
        assertTrue(requestBody.contains("\"content\":\"This is a new entry\""))
        assertTrue(requestBody.contains("\"tags\":[]"))
        assertTrue(requestBody.contains("\"emotions\":[]"))
    }

    @Test
    fun `createDiaryEntry creates a new diary entry - one Tag, no Emotions`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(201)
            .setBody("""
            {
                "id": 2,
                "title": "New Entry",
                "content": "This is a new entry",
                "createdDate": "2024-05-02",
                "userId": 1,
                "tags": [{"id": 1, "name": "Test"}],
                "emotions": []
            }
        """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val newEntry = api.createDiaryEntry("Bearer $JWT_TOKEN", CreateDiaryEntryRequest(
            "New Entry",
            "This is a new entry",
            listOf(Tag("Test")),
            emptyList()
        ))

        // Then
        assertEquals(2, newEntry.id)
        assertEquals("New Entry", newEntry.title)
        assertEquals("This is a new entry", newEntry.content)
        assertEquals(1, newEntry.tags.size)
        assertEquals(0, newEntry.emotions.size)

        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/api/v1/diary", request.path)
        assertEquals("Bearer $JWT_TOKEN", request.getHeader("Authorization"))

        val requestBody = request.body.readUtf8()
        assertTrue(requestBody.contains("\"title\":\"New Entry\""))
        assertTrue(requestBody.contains("\"content\":\"This is a new entry\""))
        assertTrue(requestBody.contains("\"tags\":[{\"name\":\"Test\"}]"))
        assertTrue(requestBody.contains("\"emotions\":[]"))
    }

    @Test
    fun `createDiaryEntry creates a new diary entry - no Tag, one Emotion`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(201)
            .setBody("""
            {
                "id": 3,
                "title": "Emotional Entry",
                "content": "This is an emotional entry",
                "createdDate": "2024-05-03",
                "userId": 1,
                "tags": [],
                "emotions": [{"id": 1, "name": "Happy"}]
            }
        """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val newEntry = api.createDiaryEntry("Bearer $JWT_TOKEN", CreateDiaryEntryRequest(
            "Emotional Entry",
            "This is an emotional entry",
            emptyList(),
            listOf(Emotion(1, "Happy"))
        ))

        // Then
        assertEquals(3, newEntry.id)
        assertEquals("Emotional Entry", newEntry.title)
        assertEquals("This is an emotional entry", newEntry.content)
        assertEquals(0, newEntry.tags.size)
        assertEquals(1, newEntry.emotions.size)

        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/api/v1/diary", request.path)
        assertEquals("Bearer $JWT_TOKEN", request.getHeader("Authorization"))

        val requestBody = request.body.readUtf8()
        assertTrue(requestBody.contains("\"title\":\"Emotional Entry\""))
        assertTrue(requestBody.contains("\"content\":\"This is an emotional entry\""))
        assertTrue(requestBody.contains("\"tags\":[]"))
        assertTrue(requestBody.contains("\"emotions\":[{\"id\":1,\"name\":\"Happy\"}]"))
    }

    @Test
    fun `createDiaryEntry creates a new diary entry - multiple Tags, multiple Emotions`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(201)
            .setBody("""
            {
                "id": 4,
                "title": "Complex Entry",
                "content": "This is a complex entry",
                "createdDate": "2024-05-04",
                "userId": 1,
                "tags": [{"id": 1, "name": "Important"}, {"id": 2, "name": "Work"}],
                "emotions": [{"id": 1, "name": "Happy"}, {"id": 2, "name": "Excited"}]
            }
        """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val newEntry = api.createDiaryEntry("Bearer $JWT_TOKEN", CreateDiaryEntryRequest(
            "Complex Entry",
            "This is a complex entry",
            listOf(Tag("Important"), Tag("Work")),
            listOf(Emotion(1, "Happy"), Emotion(2, "Excited"))
        ))

        // Then
        assertEquals(4, newEntry.id)
        assertEquals("Complex Entry", newEntry.title)
        assertEquals("This is a complex entry", newEntry.content)
        assertEquals(2, newEntry.tags.size)
        assertEquals(2, newEntry.emotions.size)

        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/api/v1/diary", request.path)
        assertEquals("Bearer $JWT_TOKEN", request.getHeader("Authorization"))

        val requestBody = request.body.readUtf8()
        assertTrue(requestBody.contains("\"title\":\"Complex Entry\""))
        assertTrue(requestBody.contains("\"content\":\"This is a complex entry\""))
        assertTrue(requestBody.contains("\"tags\":[{\"name\":\"Important\"},{\"name\":\"Work\"}]"))
        assertTrue(requestBody.contains("\"emotions\":[{\"id\":1,\"name\":\"Happy\"},{\"id\":2,\"name\":\"Excited\"}]"))
    }

    @Test
    fun `updateDiaryEntry updates an existing diary entry`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
            {
                "id": 1,
                "title": "Updated Entry",
                "content": "This is an updated entry",
                "createdDate": "2024-05-01",
                "userId": 1,
                "tags": [{"id": 1, "name": "Updated"}],
                "emotions": [{"id": 1, "name": "Excited"}]
            }
        """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val updatedEntry = api.updateDiaryEntry("Bearer $JWT_TOKEN", UpdateDiaryEntryRequest(
            id = 1,
            title = "Updated Entry",
            content = "This is an updated entry",
            tags = listOf(Tag("Updated")),
            emotions = listOf(Emotion(1, "Excited"))
        )
        )

        // Then
        assertEquals(1, updatedEntry.id)
        assertEquals("Updated Entry", updatedEntry.title)
        assertEquals("This is an updated entry", updatedEntry.content)
        assertEquals(1, updatedEntry.tags.size)
        assertEquals("Updated", updatedEntry.tags[0].name)
        assertEquals(1, updatedEntry.emotions.size)
        assertEquals("Excited", updatedEntry.emotions[0].name)

        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/api/v1/diary", request.path)
        assertEquals("Bearer $JWT_TOKEN", request.getHeader("Authorization"))

        val requestBody = request.body.readUtf8()
        assertTrue(requestBody.contains("\"id\":1"))
        assertTrue(requestBody.contains("\"title\":\"Updated Entry\""))
        assertTrue(requestBody.contains("\"content\":\"This is an updated entry\""))
        assertTrue(requestBody.contains("\"tags\":[{\"name\":\"Updated\"}]"))
        assertTrue(requestBody.contains("\"emotions\":[{\"id\":1,\"name\":\"Excited\"}]"))
    }

    @Test
    fun `deleteDiaryEntry deletes a diary entry`() = runBlocking {
        // Given
        val mockResponse = MockResponse().setResponseCode(204)
        mockWebServer.enqueue(mockResponse)

        // When
        val response = api.deleteDiaryEntry("Bearer $JWT_TOKEN", 1)

        // Then
        assertTrue(response.isSuccessful)
        assertEquals(204, response.code())

        val request = mockWebServer.takeRequest()
        assertEquals("DELETE", request.method)
        assertEquals("/api/v1/diary/1", request.path)
        assertEquals("Bearer $JWT_TOKEN", request.getHeader("Authorization"))
    }
}