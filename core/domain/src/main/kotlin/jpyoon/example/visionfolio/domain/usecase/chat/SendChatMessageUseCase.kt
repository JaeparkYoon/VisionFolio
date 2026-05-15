package jpyoon.example.visionfolio.domain.usecase.chat

import jpyoon.example.visionfolio.data.repository.ChatRepository
import jpyoon.example.visionfolio.data.repository.ChatStreamEvent
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val chatRepo: ChatRepository,
) {
    operator fun invoke(sessionId: String, content: String, model: GeminiModel): Flow<ChatStreamEvent> =
        chatRepo.sendMessage(sessionId, content, model)
}
