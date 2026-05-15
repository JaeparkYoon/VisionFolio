package jpyoon.example.visionfolio.data.portfolio.db.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.domain.model.chat.ChatMessage
import jpyoon.example.visionfolio.domain.model.chat.ChatRole

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val sessionId: String,
    val role: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
)

fun ChatMessageEntity.toDomain(): ChatMessage = ChatMessage(
    id = id,
    sessionId = sessionId,
    role = runCatching { ChatRole.valueOf(role) }.getOrDefault(ChatRole.USER),
    content = content,
    createdAt = createdAt,
)

fun ChatMessage.toEntity(): ChatMessageEntity = ChatMessageEntity(
    id = id,
    sessionId = sessionId,
    role = role.name,
    content = content,
    createdAt = createdAt,
)
