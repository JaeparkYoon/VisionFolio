package jpyoon.example.visionfolio.feature.chat

import jpyoon.example.visionfolio.core.common.ViewEffect
import jpyoon.example.visionfolio.core.common.ViewIntent
import jpyoon.example.visionfolio.core.common.ViewState
import jpyoon.example.visionfolio.domain.model.chat.ChatMessage
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel
import jpyoon.example.visionfolio.domain.model.credit.CreditBalance

data class ChatState(
    val sessions: List<ChatSession> = emptyList(),
    val messages: List<ChatMessage> = emptyList(),
    val selectedSession: ChatSession? = null,
    val inputText: String = "",
    val isLoading: Boolean = false,
    val selectedModel: GeminiModel = GeminiModel.FLASH,
    val creditBalance: CreditBalance? = null,
) : ViewState

sealed interface ChatIntent : ViewIntent {
    data class SelectSession(val session: ChatSession) : ChatIntent
    data class SendMessage(val text: String) : ChatIntent
    data class CreateSession(val title: String) : ChatIntent
    data class DeleteSession(val sessionId: Long) : ChatIntent
    data class SelectModel(val model: GeminiModel) : ChatIntent
    data class UpdateInputText(val text: String) : ChatIntent
    object NavigateBack : ChatIntent
}

sealed interface ChatEffect : ViewEffect {
    object NavigateBack : ChatEffect
    data class ShowError(val message: String) : ChatEffect
    object ShowCreditExhausted : ChatEffect
}
