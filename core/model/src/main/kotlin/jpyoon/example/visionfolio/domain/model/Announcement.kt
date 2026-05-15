package jpyoon.example.visionfolio.domain.model

data class Announcement(
    val id: String,
    val title: String,
    val body: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isHighlighted: Boolean = false,
)
