package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain

import kotlinx.coroutines.flow.Flow

interface AuthenticationService {
    suspend fun getUsername(): Flow<String?>
    suspend fun getToken(): Flow<String?>
    suspend fun login(username: String, password: String)
    suspend fun logout()
    suspend fun checkAuthentication()
}