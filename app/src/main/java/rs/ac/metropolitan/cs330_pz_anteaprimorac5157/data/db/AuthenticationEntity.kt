package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authentication")
data class AuthenticationEntity(
    @PrimaryKey val id: Long = 1,
    @ColumnInfo(name = "token") val token: String,
    @ColumnInfo(name = "username") val username: String
)