package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository

import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import kotlinx.coroutines.flow.Flow

interface DreamDiaryRepository {
    fun getDiaryEntries(): Flow<List<DiaryEntry>>
    suspend fun createDiaryEntry(title: String, content: String): DiaryEntry
    suspend fun getDiaryEntryById(id: Int): DiaryEntry
}