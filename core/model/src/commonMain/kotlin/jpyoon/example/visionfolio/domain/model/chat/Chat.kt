package jpyoon.example.visionfolio.domain.model.chat

data class ChatMessage(val id: Long = 0, val sessionId: Long, val role: ChatRole, val content: String, val timestamp: Long = 0)
data class ChatSession(val id: Long = 0, val title: String, val createdAt: Long = 0, val model: GeminiModel = GeminiModel.FLASH)
enum class GeminiModel { FLASH, PRO }
enum class ChatRole { USER, ASSISTANT }
