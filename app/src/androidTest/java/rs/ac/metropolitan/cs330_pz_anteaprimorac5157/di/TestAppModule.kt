package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db.DreamDiaryDatabase
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.AuthenticationRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.ActivityLogService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.AuthenticationService
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppHiltModule::class]
)
object TestAppModule {

    private const val TAG = "TestAppModule"

    @Provides
    @Named("test_db")
    fun provideDreamDiaryDatabase(app: Application): DreamDiaryDatabase {
        Log.d(TAG, "Providing in-memory DB...")
        return Room.inMemoryDatabaseBuilder(
            app,
            DreamDiaryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(): AuthenticationRepository {
        Log.d(TAG, "Providing FAKE AuthenticationRepository")
        return FakeAuthenticationRepository()
    }

    @Provides
    @Singleton
    fun provideDreamDiaryRepository(): DreamDiaryRepository {
        Log.d(TAG, "Providing FAKE DreamDiaryRepository")
        return FakeDreamDiaryRepository()
    }

    @Provides
    @Singleton
    fun provideActivityLogService(): ActivityLogService {
        Log.d(TAG, "Providing FAKE ActivityLogService")
        return FakeActivityLogService()
    }

    @Provides
    @Singleton
    fun provideAuthenticationService(): AuthenticationService {
        Log.d(TAG, "Providing FAKE AuthenticationService")
        return FakeAuthenticationService()
    }
}