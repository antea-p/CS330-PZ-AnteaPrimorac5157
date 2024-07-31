package rs.ac.metropolitan.cs330_pz_anteaprimorac5157

import retrofit2.Response
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.AuthenticationResponse
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.CreateDiaryEntryRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DiaryEntry
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.DreamDiaryApiService
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginRequest
import rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network.LoginResponse

class FakeDreamDiaryApiService : DreamDiaryApiService {
    private val diaryEntries = mutableListOf<DiaryEntry>()
    private var shouldThrowError = false

    fun setShouldThrowError(value: Boolean) {
        shouldThrowError = value
    }

    override suspend fun login(request: LoginRequest): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun checkAuthentication(token: String): AuthenticationResponse {
        TODO("Not yet implemented")
    }


    override suspend fun getDiaryEntries(): List<DiaryEntry> {
        if (shouldThrowError) throw Exception("Test exception")
        return diaryEntries
    }

    override suspend fun getDiaryEntryById(id: Int): DiaryEntry {
        if (shouldThrowError) throw Exception("Test exception")
        return diaryEntries.find { it.id == id } ?: throw NoSuchElementException("No entry found with id $id")
    }

    override suspend fun createDiaryEntry(entry: CreateDiaryEntryRequest): DiaryEntry {
        if (shouldThrowError) throw Exception("Test exception")
        val newEntry = DiaryEntry(
            id = diaryEntries.size + 1,
            title = entry.title,
            content = entry.content,
            createdDate = "2023-08-01",
            tags = entry.tags,
            emotions = entry.emotions
        )
        diaryEntries.add(newEntry)
        return newEntry
    }

    override suspend fun deleteDiaryEntry(id: Int): Response<Unit> {
        TODO("Not yet implemented")
    }

}