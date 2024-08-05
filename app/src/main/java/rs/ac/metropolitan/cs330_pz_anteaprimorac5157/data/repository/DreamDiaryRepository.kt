package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository

import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import kotlinx.coroutines.flow.Flow
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.EmotionEnum

interface DreamDiaryRepository {
    fun getDiaryEntries(token: String): Flow<List<DiaryEntry>>
    suspend fun createDiaryEntry(token: String, title: String, content: String, emotions: List<EmotionEnum>, tags: List<String>): DiaryEntry
    suspend fun getDiaryEntryById(token: String, id: Int): DiaryEntry
    suspend fun deleteDiaryEntry(token: String, id: Int)
}