package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.AuthenticationService

class FakeAuthenticationService : AuthenticationService {
    private val _username = MutableStateFlow<String?>(null)
    private var isAuthenticated = false

    override suspend fun getUsername(): Flow<String?> = _username

    override suspend fun getToken(): Flow<String?> = flowOf("fake-jwt-token")

    override suspend fun login(username: String, password: String) {
        if (username == "testUser" && password == "testPassword") {
            _username.value = username
            isAuthenticated = true
        } else {
            throw RuntimeException("Invalid credentials")
        }
    }

    override suspend fun logout() {
        _username.value = null
        isAuthenticated = false
    }

    override suspend fun checkAuthentication() {
        if (!isAuthenticated) {
            _username.value = null
        }
    }
}