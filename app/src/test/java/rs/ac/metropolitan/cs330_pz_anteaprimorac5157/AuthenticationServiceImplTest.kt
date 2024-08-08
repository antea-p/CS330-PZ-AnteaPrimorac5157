package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.Authentication
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.impl.AuthenticationServiceImpl
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.fakes.FakeAuthenticationRepository

@ExperimentalCoroutinesApi
class AuthenticationServiceImplTest {

    private lateinit var authRepository: FakeAuthenticationRepository
    private lateinit var authService: AuthenticationServiceImpl

    @Before
    fun setup() {
        authRepository = FakeAuthenticationRepository()
        authService = AuthenticationServiceImpl(authRepository)
    }

    @Test
    fun `getUsername returns correct Flow`() = runTest {
        // Given
        authRepository.save(Authentication("valid_token", "testUser"))

        // When
        val result = authService.getUsername().first()

        // Then
        assertEquals("testUser", result)
    }

    @Test
    fun `getUsername returns null when not authenticated`() = runTest {
        // When
        val result = authService.getUsername().first()

        // Then
        assertNull(result)
    }

    @Test
    fun `getToken returns token when authenticated and token is valid`() = runTest {
        // Given
        authRepository.save(Authentication("valid_token", "testUser"))

        // When
        val result = authService.getToken().first()

        // Then
        assertEquals("valid_token", result)
    }

    @Test
    fun `getToken returns null when not authenticated`() = runTest {
        // When
        val result = authService.getToken().first()

        // Then
        assertNull(result)
    }

    @Test
    fun `login saves authentication`() = runTest {
        // When
        authService.login("testUser", "testPassword")

        // Then
        val savedAuth = authRepository.find().first()
        assertNotNull(savedAuth)
        assertEquals("testUser", savedAuth?.username)
        assertEquals("valid_token", savedAuth?.token)
    }

    @Test(expected = RuntimeException::class)
    fun `login throws exception for invalid credentials`() = runTest {
        authService.login("wrongUser", "wrongPassword")
    }

    @Test
    fun `logout deletes authentication`() = runTest {
        // Given
        authRepository.save(Authentication("valid_token", "testUser"))

        // When
        authService.logout()

        // Then
        val result = authRepository.find().first()
        assertNull(result)
    }

    @Test
    fun `checkAuthentication deletes invalid token`() = runTest {
        // Given
        authRepository.save(Authentication("invalid_token", "testUser"))

        // When
        authService.checkAuthentication()

        // Then
        val result = authRepository.find().first()
        assertNull(result)
    }

    @Test
    fun `checkAuthentication keeps valid token`() = runTest {
        // Given
        authRepository.save(Authentication("valid_token", "testUser"))

        // When
        authService.checkAuthentication()

        // Then
        val result = authRepository.find().first()
        assertNotNull(result)
        assertEquals("testUser", result?.username)
        assertEquals("valid_token", result?.token)
    }
}