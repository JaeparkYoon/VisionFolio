package jpyoon.example.visionfolio.feature.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpyoon.example.visionfolio.core.android.MVIViewModel
import jpyoon.example.visionfolio.data.repository.ChatRepository
import jpyoon.example.visionfolio.data.repository.ChatStreamEvent
import jpyoon.example.visionfolio.data.repository.CreditRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepo: ChatRepository,
    private val creditRepo: CreditRepository,
) : MVIViewModel<ChatIntent, ChatState, ChatEffect>() {

    override fun createInitialState(): ChatState = ChatState()

    private var messagesJob: Job? = null

    init {
        chatRepo.observeSessions().onEach { sessions ->
            setState { copy(sessions = sessions) }
        }.launchIn(viewModelScope)

        creditRepo.observeBalance().onEach { balance ->
            setState { copy(creditBalance = balance.remaining) }
        }.launchIn(viewModelScope)
    }

    override suspend fun processIntent(intent: ChatIntent) {
        when (intent) {
            ChatIntent.CreateSession -> {
                val session = chatRepo.createSession("새 대화", state.value.selectedModel)
                observeMessages(session.id)
                setState { copy(activeSessionId = session.id) }
            }
            is ChatIntent.SelectSession -> {
                observeMessages(intent.sessionId)
                setState { copy(activeSessionId = intent.sessionId) }
            }
            is ChatIntent.DeleteSession -> {
                chatRepo.deleteSession(intent.sessionId)
                if (state.value.activeSessionId == intent.sessionId) {
                    messagesJob?.cancel()
                    setState { copy(activeSessionId = null, messages = emptyList()) }
                }
            }
            is ChatIntent.SetInput -> setState { copy(input = intent.text) }
            ChatIntent.SendMessage -> sendMessage()
            is ChatIntent.SelectModel -> setState { copy(selectedModel = intent.model) }
            ChatIntent.BackToSessionList -> {
                messagesJob?.cancel()
                setState { copy(activeSessionId = null, messages = emptyList()) }
            }
        }
    }

    private fun observeMessages(sessionId: String) {
        messagesJob?.cancel()
        messagesJob = chatRepo.observeMessages(sessionId).onEach { messages ->
            setState { copy(messages = messages) }
        }.launchIn(viewModelScope)
    }

    private fun sendMessage() {
        val text = state.value.input.trim()
        val sessionId = state.value.activeSessionId ?: return
        if (text.isBlank()) return

        setState { copy(input = "", isSending = true, streamingText = "") }

        viewModelScope.launch {
            creditRepo.tryDeduct(1)
            chatRepo.sendMessage(sessionId, text, state.value.selectedModel)
                .collect { event ->
                    when (event) {
                        is ChatStreamEvent.Delta -> {
                            setState { copy(streamingText = streamingText + event.text) }
                        }
                        is ChatStreamEvent.Done -> {
                            setState { copy(isSending = false, streamingText = "") }
                            setEffect { ChatEffect.ScrollToBottom }
                        }
                        is ChatStreamEvent.Error -> {
                            setState { copy(isSending = false, streamingText = "") }
                            creditRepo.refund(1)
                        }
                    }
                }
        }
    }
}
