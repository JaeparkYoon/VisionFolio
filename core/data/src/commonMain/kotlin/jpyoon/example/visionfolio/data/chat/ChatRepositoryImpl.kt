package jpyoon.example.visionfolio.data.chat

import jpyoon.example.visionfolio.core.repository.api.ChatRepository
import jpyoon.example.visionfolio.domain.model.chat.ChatMessage
import jpyoon.example.visionfolio.domain.model.chat.ChatRole
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ChatRepositoryImpl : ChatRepository {
    private val sessions = MutableStateFlow<List<ChatSession>>(emptyList())
    private val messages = MutableStateFlow<Map<Long, List<ChatMessage>>>(emptyMap())
    private var nextSessionId = 1L
    private var nextMessageId = 1L

    override fun observeSessions(): Flow<List<ChatSession>> = sessions

    override fun observeMessages(sessionId: Long): Flow<List<ChatMessage>> =
        messages.map { it[sessionId] ?: emptyList() }

    override suspend fun createSession(title: String, model: GeminiModel): ChatSession {
        val session = ChatSession(id = nextSessionId++, title = title, createdAt = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(), model = model)
        sessions.value = sessions.value + session
        return session
    }

    override suspend fun deleteSession(sessionId: Long) {
        sessions.value = sessions.value.filter { it.id != sessionId }
        messages.value = messages.value - sessionId
    }

    override suspend fun sendMessage(sessionId: Long, content: String, model: GeminiModel): ChatMessage {
        val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        val userMsg = ChatMessage(id = nextMessageId++, sessionId = sessionId, role = ChatRole.USER, content = content, timestamp = now)
        val assistantMsg = ChatMessage(id = nextMessageId++, sessionId = sessionId, role = ChatRole.ASSISTANT, content = "이것은 데모 응답입니다. 실제 AI 연동은 SnapFolio에서 제공됩니다.", timestamp = now)
        val current = messages.value[sessionId] ?: emptyList()
        messages.value = messages.value + (sessionId to (current + userMsg + assistantMsg))
        return assistantMsg
    }
}
