package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain

enum class EmotionEnum {
    NOSTALGIA, WONDER, JOY, FEAR, CURIOSITY, SHOCK, DETERMINATION, LONELINESS, HOPE
}

data class DiaryEntry(
    val id: Int,
    val title: String,
    val content: String,
    val createdDate: String,
    val tags: List<String>,
    val emotions: List<String>
)