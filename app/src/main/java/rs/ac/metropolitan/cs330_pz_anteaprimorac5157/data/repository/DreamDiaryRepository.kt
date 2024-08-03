package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository

import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import kotlinx.coroutines.flow.Flow

interface DreamDiaryRepository {
    fun getDiaryEntries(token: String): Flow<List<DiaryEntry>>
    suspend fun createDiaryEntry(token: String, title: String, content: String, emotions: List<String>, tags: List<String>): DiaryEntry
    suspend fun getDiaryEntryById(token: String, id: Int): DiaryEntry
    suspend fun deleteDiaryEntry(token: String, id: Int)
}