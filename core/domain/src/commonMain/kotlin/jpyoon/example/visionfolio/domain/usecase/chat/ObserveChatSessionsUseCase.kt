package jpyoon.example.visionfolio.domain.usecase.chat

import jpyoon.example.visionfolio.core.repository.api.ChatRepository
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

class ObserveChatSessionsUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    operator fun invoke(): Flow<List<ChatSession>> = chatRepository.observeSessions()
}
