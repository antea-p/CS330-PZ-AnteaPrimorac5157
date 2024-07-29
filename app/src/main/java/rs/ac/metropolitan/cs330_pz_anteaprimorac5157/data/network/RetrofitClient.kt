package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.89.15:3001/api/v1/"

    val instance: DreamDiaryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DreamDiaryApi::class.java)
    }
}