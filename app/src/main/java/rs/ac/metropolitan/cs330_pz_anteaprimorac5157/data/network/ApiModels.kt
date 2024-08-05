package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.data.network

data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val token: String, val message: String)
data class AuthenticationResponse(val authenticated: Boolean)

data class DiaryEntry(
    val id: Int,
    val title: String,
    val content: String,
    val createdDate: String,
    val tags: List<Tag>,
    val emotions: List<Emotion>
)

data class Tag(
    val name: String
)

data class Emotion(
    val id: Int? = null,
    val name: String
)

data class CreateDiaryEntryRequest(
    val title: String,
    val content: String,
    val tags: List<Tag> = emptyList(),
    val emotions: List<Emotion> = emptyList()
)