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
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginRequest

class ApiClientTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: DreamDiaryApiService

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
                    "token": "$expectedToken",
                    "message": "Login successful"
                }
            """.trimIndent())
        mockWebServer.enqueue(mockResponse)

        // When
        val response = api.login(LoginRequest("omori", "password"))

        // Then
        assertEquals(expectedToken, response.token)
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
        val response = api.checkAuthentication("Bearer fake-jwt-token")

        // Then
        assertTrue(response.authenticated)

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/api/v1/auth/check", request.path)
        assertEquals("Bearer fake-jwt-token", request.getHeader("Authorization"))
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
        val entries = api.getDiaryEntries()

        // Then
        assertEquals(1, entries.size)
        assertEquals("Test Entry", entries[0].title)
        assertEquals("This is a test entry", entries[0].content)
        assertEquals(1, entries[0].tags.size)
        assertEquals(1, entries[0].emotions.size)

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/api/v1/diary", request.path)
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
        val entry = api.getDiaryEntryById(1)

        // Then
        assertEquals(1, entry.id)
        assertEquals("Test Entry", entry.title)
        assertEquals("This is a test entry", entry.content)

        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/api/v1/diary/1", request.path)
    }

    // TODO: dodatni testovi ove vrste
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
        val newEntry = api.createDiaryEntry(CreateDiaryEntryRequest("New Entry", "This is a new entry", 1))

        // Then
        assertEquals(2, newEntry.id)
        assertEquals("New Entry", newEntry.title)
        assertEquals("This is a new entry", newEntry.content)

        val request = mockWebServer.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/api/v1/diary", request.path)
        val requestBody = request.body.readUtf8()
        assertTrue(requestBody.contains("\"title\":\"New Entry\""))
        assertTrue(requestBody.contains("\"content\":\"This is a new entry\""))
        assertTrue(requestBody.contains("\"userId\":1"))
    }

    @Test
    fun `deleteDiaryEntry deletes a diary entry`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(204)
        mockWebServer.enqueue(mockResponse)

        // When
        val response = api.deleteDiaryEntry(1)

        // Then
        assertTrue(response.isSuccessful)
        assertEquals(204, response.code())

        val request = mockWebServer.takeRequest()
        assertEquals("DELETE", request.method)
        assertEquals("/api/v1/diary/1", request.path)
    }
}