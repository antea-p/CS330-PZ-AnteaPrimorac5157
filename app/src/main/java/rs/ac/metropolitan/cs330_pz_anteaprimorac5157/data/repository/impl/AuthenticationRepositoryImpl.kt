package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.AuthenticationDao
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DreamDiaryApiService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginResponse
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationMapper
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.Authentication
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationDao: AuthenticationDao,
    private val dreamDiaryApiService: DreamDiaryApiService,
    private val authenticationMapper: AuthenticationMapper
) : AuthenticationRepository {

    override suspend fun find(): Flow<Authentication?> =
        authenticationDao.find().map { it?.let { authenticationMapper.mapToDomain(it) } }

    override suspend fun save(authentication: Authentication) {
        authenticationDao.save(authenticationMapper.mapToEntity(authentication))
    }

    override suspend fun delete() {
        authenticationDao.delete()
    }

    override suspend fun checkTokenValidity(token: String): Boolean {
        return try {
            dreamDiaryApiService.checkAuthentication("Bearer $token").authenticated
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun login(username: String, password: String): LoginResponse {
        return dreamDiaryApiService.login(LoginRequest(username, password))
    }
}