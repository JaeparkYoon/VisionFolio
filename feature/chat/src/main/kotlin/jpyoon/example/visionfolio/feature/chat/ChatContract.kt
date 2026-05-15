package jpyoon.example.visionfolio.feature.chat

import jpyoon.example.visionfolio.core.android.ViewEffect
import jpyoon.example.visionfolio.core.android.ViewIntent
import jpyoon.example.visionfolio.core.android.ViewState
import jpyoon.example.visionfolio.domain.model.chat.ChatMessage
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel

data class ChatState(
    val sessions: List<ChatSession> = emptyList(),
    val activeSessionId: String? = null,
    val messages: List<ChatMessage> = emptyList(),
    val input: String = "",
    val streamingText: String = "",
    val isSending: Boolean = false,
    val selectedModel: GeminiModel = GeminiModel.FLASH,
    val creditBalance: Int = 999,
) : ViewState

sealed interface ChatIntent : ViewIntent {
    object CreateSession : ChatIntent
    data class SelectSession(val sessionId: String) : ChatIntent
    data class DeleteSession(val sessionId: String) : ChatIntent
    data class SetInput(val text: String) : ChatIntent
    object SendMessage : ChatIntent
    data class SelectModel(val model: GeminiModel) : ChatIntent
    object BackToSessionList : ChatIntent
}

sealed interface ChatEffect : ViewEffect {
    object ScrollToBottom : ChatEffect
}
