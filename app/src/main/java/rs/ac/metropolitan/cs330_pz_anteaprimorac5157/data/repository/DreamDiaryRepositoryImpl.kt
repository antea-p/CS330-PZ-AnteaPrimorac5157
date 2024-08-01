package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository

import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DreamDiaryApiService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.CreateDiaryEntryRequest
import javax.inject.Inject

class DreamDiaryRepositoryImpl @Inject constructor(
    private val apiService: DreamDiaryApiService
) : DreamDiaryRepository {

    override fun getDiaryEntries(): Flow<List<DiaryEntry>> = flow {
        val entries = apiService.getDiaryEntries().map { it.toDomain() }
        emit(entries)
    }

    // TODO: tags, emotions
    override suspend fun createDiaryEntry(title: String, content: String): DiaryEntry {
        val request = CreateDiaryEntryRequest(title, content, emptyList(), emptyList())
        return apiService.createDiaryEntry(request).toDomain()
    }

    // TODO
    override suspend fun getDiaryEntryById(id: Int): DiaryEntry {
        return apiService.getDiaryEntryById(id).toDomain()
    }

    override suspend fun deleteDiaryEntry(id: Int) {
        val response = apiService.deleteDiaryEntry(id)
        if (!response.isSuccessful) {
            throw Exception("Failed to delete entry: ${response.message()}")
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