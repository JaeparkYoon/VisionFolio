package jpyoon.example.visionfolio.domain.usecase.chat

import jpyoon.example.visionfolio.core.repository.api.ChatRepository
import me.tatarka.inject.annotations.Inject

class DeleteChatSessionUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(sessionId: Long) = chatRepository.deleteSession(sessionId)
}
