package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthenticationDao {
    @Query("SELECT * FROM authentication WHERE id = 1")
    fun find(): Flow<AuthenticationEntity?>

    @Query("DELETE FROM authentication")
    suspend fun delete()

    @Upsert
    suspend fun save(authenticationEntity: AuthenticationEntity)
}
