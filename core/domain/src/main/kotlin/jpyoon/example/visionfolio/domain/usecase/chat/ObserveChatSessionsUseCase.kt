package jpyoon.example.visionfolio.domain.usecase.chat

import jpyoon.example.visionfolio.data.repository.ChatRepository
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatSessionsUseCase @Inject constructor(
    private val chatRepo: ChatRepository,
) {
    operator fun invoke(): Flow<List<ChatSession>> = chatRepo.observeSessions()
}
