package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface DreamDiaryApiService {
    @POST("api/v1/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/v1/auth/check")
    suspend fun checkAuthentication(@Header("Authorization") token: String): AuthenticationResponse

    @GET("api/v1/diary")
    suspend fun getDiaryEntries(@Header("Authorization") token: String): List<DiaryEntry>

    @GET("api/v1/diary/{id}")
    suspend fun getDiaryEntryById(@Header("Authorization") token: String, @Path("id") id: Int): DiaryEntry

    @POST("api/v1/diary")
    suspend fun createDiaryEntry(@Header("Authorization") token: String, @Body entry: CreateDiaryEntryRequest): DiaryEntry

    @DELETE("api/v1/diary/{id}")
    suspend fun deleteDiaryEntry(@Header("Authorization") token: String, @Path("id") id: Int): Response<Unit>
}