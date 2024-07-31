package rs.ac.metropolitan.cs330_pz_anteaprimorac5157.domain

// TODO: promjena tipa za emotions
data class DiaryEntry(
    val id: Int,
    val title: String,
    val content: String,
    val createdDate: String,
    val tags: List<String>,
    val emotions: List<String>
)