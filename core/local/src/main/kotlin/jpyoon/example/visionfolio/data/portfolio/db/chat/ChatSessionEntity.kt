package jpyoon.example.visionfolio.data.portfolio.db.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel

@Entity(tableName = "chat_sessions")
data class ChatSessionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val model: String = GeminiModel.FLASH.name,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

fun ChatSessionEntity.toDomain(): ChatSession = ChatSession(
    id = id,
    title = title,
    model = runCatching { GeminiModel.valueOf(model) }.getOrDefault(GeminiModel.FLASH),
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun ChatSession.toEntity(): ChatSessionEntity = ChatSessionEntity(
    id = id,
    title = title,
    model = model.name,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
