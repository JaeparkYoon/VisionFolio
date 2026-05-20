package jpyoon.example.visionfolio.domain.usecase.chat

import jpyoon.example.visionfolio.core.repository.api.ChatRepository
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel
import me.tatarka.inject.annotations.Inject

class CreateChatSessionUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(title: String, model: GeminiModel = GeminiModel.FLASH): ChatSession =
        chatRepository.createSession(title, model)
}
