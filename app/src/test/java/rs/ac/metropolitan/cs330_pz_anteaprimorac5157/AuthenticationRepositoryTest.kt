package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.AuthenticationDao
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.AuthenticationEntity
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.AuthenticationResponse
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DreamDiaryApiService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginResponse
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationMapper
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepositoryImpl
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.Authentication

@ExperimentalCoroutinesApi
class AuthenticationRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var authenticationDao: AuthenticationDao
    private lateinit var dreamDiaryApiService: DreamDiaryApiService
    private lateinit var authenticationMapper: AuthenticationMapper
    private lateinit var repository: AuthenticationRepositoryImpl

    @Before
    fun setup() {
        authenticationDao = mockk()
        dreamDiaryApiService = mockk()
        authenticationMapper = mockk()
        repository = AuthenticationRepositoryImpl(authenticationDao, dreamDiaryApiService, authenticationMapper)
    }

    @Test
    fun `find returns mapped authentication when exists`() = runTest {
        // Given
        val authEntity = AuthenticationEntity(token = "test_token", username = "test_user")
        val authDomain = Authentication(token = "test_token", username = "test_user")
        every { authenticationDao.find() } returns flowOf(authEntity)
        every { authenticationMapper.mapToDomain(authEntity) } returns authDomain

        // When
        val result = repository.find().first()

        // Then
        assert(result == authDomain)
        verify { authenticationDao.find() }
        verify { authenticationMapper.mapToDomain(authEntity) }
    }

    @Test
    fun `find returns null when no authentication exists`() = runTest {
        // Given
        every { authenticationDao.find() } returns flowOf(null)

        // When
        val result = repository.find().first()

        // Then
        assert(result == null)
        verify { authenticationDao.find() }
        verify(exactly = 0) { authenticationMapper.mapToDomain(any()) }
    }

    @Test
    fun `save calls dao with mapped entity`() = runTest {
        // Given
        val authDomain = Authentication(token = "test_token", username = "test_user")
        val authEntity = AuthenticationEntity(token = "test_token", username = "test_user")
        every { authenticationMapper.mapToEntity(authDomain) } returns authEntity
        coEvery { authenticationDao.save(authEntity) } just Runs

        // When
        repository.save(authDomain)

        // Then
        verify { authenticationMapper.mapToEntity(authDomain) }
        coVerify { authenticationDao.save(authEntity) }
    }

    @Test
    fun `delete calls dao delete method`() = runTest {
        // Given
        coEvery { authenticationDao.delete() } just Runs

        // When
        repository.delete()

        // Then
        coVerify { authenticationDao.delete() }
    }

    @Test
    fun `checkTokenValidity returns true for valid token`() = runTest {
        // Given
        val token = "valid_token"
        coEvery { dreamDiaryApiService.checkAuthentication("Bearer $token") } returns AuthenticationResponse(authenticated = true)

        // When
        val result = repository.checkTokenValidity(token)

        // Then
        assert(result)
        coVerify { dreamDiaryApiService.checkAuthentication("Bearer $token") }
    }

    @Test
    fun `checkTokenValidity returns false for invalid token`() = runTest {
        // Given
        val token = "invalid_token"
        coEvery { dreamDiaryApiService.checkAuthentication("Bearer $token") } returns AuthenticationResponse(authenticated = false)

        // When
        val result = repository.checkTokenValidity(token)

        // Then
        assert(!result)
        coVerify { dreamDiaryApiService.checkAuthentication("Bearer $token") }
    }

    @Test
    fun `checkTokenValidity returns false when api throws exception`() = runTest {
        // Given
        val token = "error_token"
        coEvery { dreamDiaryApiService.checkAuthentication("Bearer $token") } throws RuntimeException("API Error")

        // When
        val result = repository.checkTokenValidity(token)

        // Then
        assert(!result)
        coVerify { dreamDiaryApiService.checkAuthentication("Bearer $token") }
    }


    @Test
    fun `login calls api service and returns login response`() = runTest {
        // Given
        val username = "testUser"
        val password = "testPassword"
        val loginRequest = LoginRequest(username, password)
        val loginResponse = LoginResponse("test_token", "Login successful")
        coEvery { dreamDiaryApiService.login(loginRequest) } returns loginResponse

        // When
        val result = repository.login(username, password)

        // Then
        assert(result == loginResponse)
        coVerify { dreamDiaryApiService.login(loginRequest) }
    }

    @Test
    fun `login throws exception when api service fails`() = runTest {
        // Given
        val username = "testUser"
        val password = "testPassword"
        val loginRequest = LoginRequest(username, password)
        coEvery { dreamDiaryApiService.login(loginRequest) } throws RuntimeException("API Error")

        // When & Then
        try {
            repository.login(username, password)
            assert(false) { "Expected exception was not thrown" }
        } catch (e: RuntimeException) {
            assert(e.message == "API Error")
        }
        coVerify { dreamDiaryApiService.login(loginRequest) }
    }
}