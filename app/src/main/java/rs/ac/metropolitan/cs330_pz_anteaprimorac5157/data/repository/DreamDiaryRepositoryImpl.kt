package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository

import android.util.Log
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DreamDiaryApiService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.CreateDiaryEntryRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.Emotion
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.Tag
import javax.inject.Inject

class DreamDiaryRepositoryImpl @Inject constructor(
    private val apiService: DreamDiaryApiService
) : DreamDiaryRepository {

    override fun getDiaryEntries(token: String): Flow<List<DiaryEntry>> = flow {
        val entries = apiService.getDiaryEntries("Bearer $token").map { it.toDomain() }
        emit(entries)
    }

    override suspend fun createDiaryEntry(token: String, title: String, content: String, emotions: List<String>, tags: List<String>): DiaryEntry {
        val request = CreateDiaryEntryRequest(
            title = title,
            content = content,
            emotions = emotions,
            tags = tags
        )
        Log.d("DreamDiaryRepository", "Request: $request")
        return apiService.createDiaryEntry("Bearer $token", request).toDomain()
    }

    // TODO
    override suspend fun getDiaryEntryById(token: String, id: Int): DiaryEntry {
        try {
            val response = apiService.getDiaryEntryById("Bearer $token", id)
            return response.toDomain()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteDiaryEntry(token: String, id: Int) {
        try {
            val response = apiService.deleteDiaryEntry("Bearer $token", id)
//            if (!response.isSuccessful) {
//                throw Exception("Failed to delete entry: ${response.message()}")
//            }
        } catch (e: Exception) {
            throw e
        }
    }

    // TODO: mapper
    private fun rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DiaryEntry.toDomain(): DiaryEntry {
        return DiaryEntry(
            id = this.id,
            title = this.title,
            content = this.content,
            createdDate = this.createdDate,
            tags = this.tags.map { it.name },
            emotions = this.emotions.map { it.name }
        )
    }
}