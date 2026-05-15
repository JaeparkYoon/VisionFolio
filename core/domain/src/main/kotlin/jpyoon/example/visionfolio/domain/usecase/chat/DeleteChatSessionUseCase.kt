package jpyoon.example.visionfolio.domain.usecase.chat

import jpyoon.example.visionfolio.data.repository.ChatRepository
import javax.inject.Inject

class DeleteChatSessionUseCase @Inject constructor(
    private val chatRepo: ChatRepository,
) {
    suspend operator fun invoke(sessionId: String) = chatRepo.deleteSession(sessionId)
}
