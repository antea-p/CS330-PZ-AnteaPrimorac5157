package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginResponse
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.Authentication

class FakeAuthenticationRepository : AuthenticationRepository {
    private var storedAuthentication: Authentication? = null
    private val validToken = "valid_token"
    private val validUsername = "testUser"
    private val validPassword = "testPassword"

    override suspend fun find(): Flow<Authentication?> = flowOf(storedAuthentication)

    override suspend fun save(authentication: Authentication) {
        storedAuthentication = authentication
    }

    override suspend fun delete() {
        storedAuthentication = null
    }

    override suspend fun checkTokenValidity(token: String): Boolean {
        return token == validToken
    }

    override suspend fun login(username: String, password: String): LoginResponse {
        return if (username == validUsername && password == validPassword) {
            LoginResponse(validToken, "Login successful")
        } else {
            throw RuntimeException("Invalid credentials")
        }
    }
}