package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DreamDiaryApiService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.RetrofitHelper
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.RetrofitHelperImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppHiltModule {
    private const val TAG = "AppHiltModule";

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext

    }

    @Provides
    @Singleton
    fun provideRetrofitHelper(): RetrofitHelper {
        Log.d(TAG, "Providing RetrofitHelper")
        return RetrofitHelperImpl()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofitHelper: RetrofitHelper): DreamDiaryApiService {
        Log.d(TAG, "Providing ApiService")
        return retrofitHelper.getApiService()
    }

}