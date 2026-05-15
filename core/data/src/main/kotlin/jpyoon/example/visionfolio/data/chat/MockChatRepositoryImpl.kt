package jpyoon.example.visionfolio.data.chat

import jpyoon.example.visionfolio.data.portfolio.db.chat.ChatDao
import jpyoon.example.visionfolio.data.portfolio.db.chat.toDomain
import jpyoon.example.visionfolio.data.portfolio.db.chat.toEntity
import jpyoon.example.visionfolio.data.repository.ChatRepository
import jpyoon.example.visionfolio.data.repository.ChatStreamEvent
import jpyoon.example.visionfolio.domain.model.chat.ChatMessage
import jpyoon.example.visionfolio.domain.model.chat.ChatRole
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
) : ChatRepository {

    override fun observeSessions(): Flow<List<ChatSession>> =
        chatDao.observeSessions().map { list -> list.map { it.toDomain() } }

    override fun observeMessages(sessionId: String): Flow<List<ChatMessage>> =
        chatDao.observeMessages(sessionId).map { list -> list.map { it.toDomain() } }

    override suspend fun createSession(title: String, model: GeminiModel): ChatSession {
        val session = ChatSession(
            id = UUID.randomUUID().toString(),
            title = title,
            model = model,
        )
        chatDao.upsertSession(session.toEntity())
        return session
    }

    override suspend fun deleteSession(sessionId: String) {
        chatDao.deleteMessagesForSession(sessionId)
        chatDao.deleteSession(sessionId)
    }

    override fun sendMessage(
        sessionId: String,
        content: String,
        model: GeminiModel,
    ): Flow<ChatStreamEvent> = flow {
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sessionId = sessionId,
            role = ChatRole.USER,
            content = content,
        )
        chatDao.insertMessage(userMsg.toEntity())

        delay(300)

        val response = MOCK_RESPONSES.random()
        val words = response.split(" ")
        val sb = StringBuilder()
        for (word in words) {
            if (sb.isNotEmpty()) sb.append(" ")
            sb.append(word)
            emit(ChatStreamEvent.Delta(sb.toString()))
            delay(50)
        }

        val assistantMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sessionId = sessionId,
            role = ChatRole.ASSISTANT,
            content = response,
        )
        chatDao.insertMessage(assistantMsg.toEntity())
        chatDao.upsertSession(
            ChatSession(id = sessionId, title = content.take(30), model = model, updatedAt = System.currentTimeMillis())
                .toEntity()
        )
        emit(ChatStreamEvent.Done(assistantMsg))
    }

    companion object {
        private val MOCK_RESPONSES = listOf(
            "현재 포트폴리오는 기술주 비중이 높은 편입니다. 분산 투자를 위해 채권이나 원자재 비중을 늘려보는 것도 고려해 볼 수 있습니다.",
            "최근 시장 변동성이 높아지고 있습니다. 장기적 관점에서 정기적인 리밸런싱을 추천드립니다.",
            "해외 주식의 비중이 적절한 수준입니다. 환율 변동에 따른 리스크 관리를 위해 환헤지 ETF도 검토해 보세요.",
            "배당 수익률 관점에서 현재 포트폴리오의 연간 예상 배당금은 약 2.5% 수준입니다.",
            "암호화폐 비중이 전체의 약 20%를 차지하고 있어, 변동성이 클 수 있습니다. 리스크 허용 범위를 재점검해 보세요.",
        )
    }
}
