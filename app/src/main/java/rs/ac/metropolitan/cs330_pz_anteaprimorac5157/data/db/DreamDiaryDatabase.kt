package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db

import android.content.Context
import androidx.room.*
import androidx.room.Room

@Database(entities = [AuthenticationEntity::class, ActivityLogEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class DreamDiaryDatabase : RoomDatabase() {
    abstract fun authenticationDao(): AuthenticationDao
    abstract fun activityLogDao(): ActivityLogDao

}