package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface RetrofitHelper {
    fun getApiService(): DreamDiaryApiService
}

class RetrofitHelperImpl : RetrofitHelper {
    override fun getApiService(): DreamDiaryApiService {
        return getInstance().create(DreamDiaryApiService::class.java)
    }

    private val gson = GsonBuilder().create()
    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().setLevel(
                            HttpLoggingInterceptor.Level.BODY
                        )
                    )
                    .build()
            )
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
