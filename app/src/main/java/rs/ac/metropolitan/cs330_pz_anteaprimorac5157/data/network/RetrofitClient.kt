package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.Constants.BASE_URL

object RetrofitClient {

    val instance: DreamDiaryApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DreamDiaryApi::class.java)
    }
}