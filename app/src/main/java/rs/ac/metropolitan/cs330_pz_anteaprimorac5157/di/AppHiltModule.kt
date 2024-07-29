package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.ActivityLogDao
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.AuthenticationDao
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.DreamDiaryDatabase
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DreamDiaryApiService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.RetrofitHelper
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.RetrofitHelperImpl
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationMapper
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepositoryImpl
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

    @Provides
    @Singleton
    fun provideDreamDiaryDatabase(app: Application): DreamDiaryDatabase {
        return Room.databaseBuilder(
            app,
            DreamDiaryDatabase::class.java,
            "dream_diary_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideAuthenticationDao(database: DreamDiaryDatabase): AuthenticationDao {
        return database.authenticationDao()
    }

    @Provides
    @Singleton
    fun provideActivityLogDao(database: DreamDiaryDatabase): ActivityLogDao {
        return database.activityLogDao()
    }

    @Provides
    fun provideAuthenticationMapper(): AuthenticationMapper {
        return AuthenticationMapper()
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        authenticationDao: AuthenticationDao,
        dreamDiaryApiService: DreamDiaryApiService,
        authenticationMapper: AuthenticationMapper
    ): AuthenticationRepository {
        Log.d(TAG, "Providing AuthenticationRepository")
        return AuthenticationRepositoryImpl(authenticationDao, dreamDiaryApiService, authenticationMapper)
    }
}