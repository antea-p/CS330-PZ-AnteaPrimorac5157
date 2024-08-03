package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationServiceImpl @Inject constructor(
    private val authRepository: AuthenticationRepository
) : AuthenticationService {

    override suspend fun getUsername(): Flow<String?> =
        authRepository.find().map { it?.username }

    override suspend fun getToken(): Flow<String?> =
        authRepository.find().map { it?.token }

    override suspend fun login(username: String, password: String) {
        val loginResponse = authRepository.login(username, password)
        authRepository.save(Authentication(loginResponse.token, username))
    }

    override suspend fun logout() {
        authRepository.delete()
    }

    override suspend fun checkAuthentication() {
        val auth = authRepository.find().first()
        if (auth != null) {
            val isValid = authRepository.checkTokenValidity(auth.token)
            if (!isValid) {
                authRepository.delete()
            }
        }
    }
}