package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository

import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.AuthenticationEntity
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.Authentication
import javax.inject.Inject

class AuthenticationMapper @Inject constructor() {
    fun mapToDomain(entity: AuthenticationEntity): Authentication =
        Authentication(token = entity.token, username = entity.username)

    fun mapToEntity(domain: Authentication): AuthenticationEntity =
        AuthenticationEntity(token = domain.token, username = domain.username)
}