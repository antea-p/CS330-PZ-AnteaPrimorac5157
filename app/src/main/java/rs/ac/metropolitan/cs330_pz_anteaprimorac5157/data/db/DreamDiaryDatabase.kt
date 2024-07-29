package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db

import android.content.Context
import androidx.room.*
import androidx.room.Room

// TODO: HILT
@Database(entities = [AuthenticationEntity::class, ActivityLogEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class DreamDiaryDatabase : RoomDatabase() {
    abstract fun authenticationRepository(): AuthenticationDao
    abstract fun activityLogRepository(): ActivityLogDao

    companion object {
        @Volatile
        private var INSTANCE: DreamDiaryDatabase? = null

        // TODO: HILT
        // @Provides
        // @Singleton
        fun getDatabase(context: Context): DreamDiaryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DreamDiaryDatabase::class.java,
                    "dream_diary_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}