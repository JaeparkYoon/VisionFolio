package jpyoon.example.visionfolio.domain.model.chat

enum class ChatRole { USER, ASSISTANT }

enum class GeminiModel(val label: String) {
    FLASH("Flash"),
    PRO("Pro"),
}

data class ChatMessage(
    val id: String,
    val sessionId: String,
    val role: ChatRole,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
)

data class ChatSession(
    val id: String,
    val title: String,
    val model: GeminiModel = GeminiModel.FLASH,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
