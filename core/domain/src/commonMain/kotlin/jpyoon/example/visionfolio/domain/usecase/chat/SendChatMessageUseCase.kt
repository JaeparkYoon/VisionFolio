package jpyoon.example.visionfolio.domain.usecase.chat

import jpyoon.example.visionfolio.core.repository.api.ChatRepository
import jpyoon.example.visionfolio.core.repository.api.CreditRepository
import jpyoon.example.visionfolio.domain.model.chat.ChatMessage
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel
import me.tatarka.inject.annotations.Inject

class SendChatMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val creditRepository: CreditRepository,
) {
    suspend operator fun invoke(sessionId: Long, content: String, model: GeminiModel): Result<ChatMessage> =
        runCatching {
            creditRepository.deduct(1)
            chatRepository.sendMessage(sessionId, content, model)
        }
}
