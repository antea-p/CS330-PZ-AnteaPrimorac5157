package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.fakes

import okhttp3.ResponseBody
import retrofit2.HttpException
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
    private var shouldThrowHttpException = false

    fun setShouldThrowError(value: Boolean) {
        shouldThrowError = value
        shouldThrowHttpException = false
    }

    private fun throwExceptionIfNeeded() {
        when {
            shouldThrowError -> throw Exception("Test exception")
            shouldThrowHttpException -> throw HttpException(
                Response.error<Any>(500, ResponseBody.create(null, "HTTP Exception"))
            )
        }
    }

    override suspend fun login(request: LoginRequest): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun checkAuthentication(token: String): AuthenticationResponse {
        TODO("Not yet implemented")
    }


    override suspend fun getDiaryEntries(token: String): List<DiaryEntry> {
        throwExceptionIfNeeded()
        return diaryEntries
    }

    override suspend fun getDiaryEntryById(token: String, id: Int): DiaryEntry {
        throwExceptionIfNeeded()
        return diaryEntries.find { it.id == id }
            ?: throw NoSuchElementException("No entry found with id $id")
    }

    override suspend fun createDiaryEntry(token: String, entry: CreateDiaryEntryRequest): DiaryEntry {
        throwExceptionIfNeeded()
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

    override suspend fun deleteDiaryEntry(token: String, id: Int): Response<Unit> {
        throwExceptionIfNeeded()
        val entryIndex = diaryEntries.indexOfFirst { it.id == id }
        return if (entryIndex != -1) {
            diaryEntries.removeAt(entryIndex)
            Response.success(Unit)
        } else {
            Response.error(404, ResponseBody.create(null, "Entry not found"))
        }
    }

    fun clear() {
        diaryEntries.clear()
        shouldThrowError = false
        shouldThrowHttpException = false
    }
}