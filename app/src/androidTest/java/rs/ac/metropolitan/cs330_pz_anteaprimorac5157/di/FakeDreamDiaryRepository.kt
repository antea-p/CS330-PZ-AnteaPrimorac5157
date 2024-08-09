package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.di

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.repository.DreamDiaryRepository
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.DiaryEntry
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain.EmotionEnum

class FakeDreamDiaryRepository : DreamDiaryRepository {
    private val diaryEntries = mutableListOf<DiaryEntry>()
    private var nextId = 1

    override fun getDiaryEntries(token: String): Flow<List<DiaryEntry>> = flowOf(diaryEntries)

    override suspend fun createDiaryEntry(
        token: String,
        title: String,
        content: String,
        emotions: List<EmotionEnum>,
        tags: List<String>
    ): DiaryEntry {
        val newEntry = DiaryEntry(
            id = nextId++,
            title = title,
            content = content,
            createdDate = "2023-08-01",
            emotions = emotions.map { it.name },
            tags = tags
        )
        diaryEntries.add(newEntry)
        return newEntry
    }

    override suspend fun getDiaryEntryById(token: String, id: Int): DiaryEntry {
        return diaryEntries.find { it.id == id }
            ?: throw NoSuchElementException("No diary entry found with id $id")
    }

    override suspend fun updateDiaryEntry(
        token: String,
        id: Int,
        title: String,
        content: String,
        emotions: List<EmotionEnum>,
        tags: List<String>
    ): DiaryEntry {
        val existingEntryIndex = diaryEntries.indexOfFirst { it.id == id }
        if (existingEntryIndex != -1) {
            val updatedEntry = DiaryEntry(
                id = id,
                title = title,
                content = content,
                createdDate = diaryEntries[existingEntryIndex].createdDate,
                emotions = emotions.map { it.name },
                tags = tags
            )
            diaryEntries[existingEntryIndex] = updatedEntry
            return updatedEntry
        } else {
            throw NoSuchElementException("No diary entry found with id $id")
        }
    }

    override suspend fun deleteDiaryEntry(token: String, id: Int) {
        val entryToRemove = diaryEntries.find { it.id == id }
        if (entryToRemove != null) {
            diaryEntries.remove(entryToRemove)
        } else {
            throw NoSuchElementException("No diary entry found with id $id")
        }
    }

}