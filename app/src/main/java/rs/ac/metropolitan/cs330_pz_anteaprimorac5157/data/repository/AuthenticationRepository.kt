package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository

import kotlinx.coroutines.flow.Flow
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginResponse
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.Authentication

interface AuthenticationRepository {
    suspend fun find(): Flow<Authentication?>
    suspend fun save(authentication: Authentication)
    suspend fun delete()
    suspend fun checkTokenValidity(token: String): Boolean
    suspend fun login(username: String, password: String): LoginResponse
}