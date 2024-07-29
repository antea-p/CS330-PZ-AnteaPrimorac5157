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
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DreamDiaryApi
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginRequest

class ApiClientTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var api: DreamDiaryApi

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
            .create(DreamDiaryApi::class.java)
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
        println("Actual response: $response")
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


}