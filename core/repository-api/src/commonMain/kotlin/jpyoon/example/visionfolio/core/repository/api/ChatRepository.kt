package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.chat.ChatMessage
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeSessions(): Flow<List<ChatSession>>
    fun observeMessages(sessionId: Long): Flow<List<ChatMessage>>
    suspend fun createSession(title: String, model: GeminiModel): ChatSession
    suspend fun deleteSession(sessionId: Long)
    suspend fun sendMessage(sessionId: Long, content: String, model: GeminiModel): ChatMessage
}
