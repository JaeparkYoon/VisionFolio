package jpyoon.example.visionfolio.domain.usecase.chat

import jpyoon.example.visionfolio.data.repository.ChatRepository
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel
import javax.inject.Inject

class CreateChatSessionUseCase @Inject constructor(
    private val chatRepo: ChatRepository,
) {
    suspend operator fun invoke(title: String = "새 대화", model: GeminiModel = GeminiModel.FLASH): ChatSession =
        chatRepo.createSession(title, model)
}
