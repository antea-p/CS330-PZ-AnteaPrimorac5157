package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain

// TODO: promjena tipa za emotions
enum class Emotion {
    HAPPY, SAD, ANGRY, EXCITED, NERVOUS, CALM, FRUSTRATED, CONTENT, ANXIOUS, NOSTALGIC
}

data class DiaryEntry(
    val id: Int,
    val title: String,
    val content: String,
    val createdDate: String,
    val tags: List<String>,
    val emotions: List<String>
)