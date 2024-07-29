package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface DreamDiaryApi {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/v1/auth/check")
    suspend fun checkAuthentication(@Header("Authorization") token: String): AuthenticationResponse
}