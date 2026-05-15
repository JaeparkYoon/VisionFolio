package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.chat.ChatMessage
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel
import kotlinx.coroutines.flow.Flow

sealed interface ChatStreamEvent {
    data class Delta(val text: String) : ChatStreamEvent
    data class Done(val fullMessage: ChatMessage) : ChatStreamEvent
    data class Error(val throwable: Throwable) : ChatStreamEvent
}

interface ChatRepository {
    fun observeSessions(): Flow<List<ChatSession>>
    fun observeMessages(sessionId: String): Flow<List<ChatMessage>>
    suspend fun createSession(title: String, model: GeminiModel): ChatSession
    suspend fun deleteSession(sessionId: String)
    fun sendMessage(sessionId: String, content: String, model: GeminiModel): Flow<ChatStreamEvent>
}
