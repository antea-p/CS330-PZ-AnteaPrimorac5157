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
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.ActivityLogRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.impl.ActivityLogRepositoryImpl
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationMapper
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.impl.AuthenticationRepositoryImpl
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.impl.DreamDiaryRepositoryImpl
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.ActivityLogService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.impl.ActivityLogServiceImpl
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.AuthenticationService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.impl.AuthenticationServiceImpl
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
    @Singleton
    fun provideActivityLogRepository(activityLogDao: ActivityLogDao): ActivityLogRepository {
        return ActivityLogRepositoryImpl(activityLogDao)
    }

    @Provides
    @Singleton
    fun provideActivityLogService(repository: ActivityLogRepository): ActivityLogService {
        return ActivityLogServiceImpl(repository)
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

    @Provides
    @Singleton
    fun provideAuthenticationService(authRepository: AuthenticationRepository): AuthenticationService {
        return AuthenticationServiceImpl(authRepository)
    }

    @Provides
    @Singleton
    fun provideDreamDiaryRepository(apiService: DreamDiaryApiService): DreamDiaryRepository {
        return DreamDiaryRepositoryImpl(apiService)
    }
}
