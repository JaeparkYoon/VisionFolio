package jpyoon.example.visionfolio.feature.chat

import jpyoon.example.visionfolio.core.common.MVIViewModel
import jpyoon.example.visionfolio.core.repository.api.ChatRepository
import jpyoon.example.visionfolio.core.repository.api.CreditRepository
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.usecase.chat.CreateChatSessionUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.DeleteChatSessionUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.ObserveChatSessionsUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.SendChatMessageUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class ChatViewModel @Inject constructor(
    private val observeSessions: ObserveChatSessionsUseCase,
    private val sendMessage: SendChatMessageUseCase,
    private val createSession: CreateChatSessionUseCase,
    private val deleteSession: DeleteChatSessionUseCase,
    private val creditRepo: CreditRepository,
    private val chatRepo: ChatRepository,
) : MVIViewModel<ChatIntent, ChatState, ChatEffect>() {

    override fun createInitialState(): ChatState = ChatState(isLoading = true)

    private var messagesJob: Job? = null

    init {
        observeSessions().onEach { sessions ->
            setState { copy(sessions = sessions, isLoading = false) }
            if (currentState.selectedSession == null && sessions.isNotEmpty()) {
                val latest = sessions.maxByOrNull { it.createdAt }!!
                setState { copy(selectedSession = latest) }
                observeMessagesFor(latest.id)
            }
        }.launchIn(viewModelScope)

        creditRepo.observeBalance().onEach { balance ->
            setState { copy(creditBalance = balance) }
        }.launchIn(viewModelScope)
    }

    override suspend fun processIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.SelectSession -> handleSelectSession(intent.session)
            is ChatIntent.SendMessage -> handleSendMessage(intent.text)
            is ChatIntent.CreateSession -> handleCreateSession(intent.title)
            is ChatIntent.DeleteSession -> handleDeleteSession(intent.sessionId)
            is ChatIntent.SelectModel -> setState { copy(selectedModel = intent.model) }
            is ChatIntent.UpdateInputText -> setState { copy(inputText = intent.text) }
            ChatIntent.NavigateBack -> setEffect { ChatEffect.NavigateBack }
        }
    }

    private fun handleSelectSession(session: ChatSession) {
        setState { copy(selectedSession = session) }
        observeMessagesFor(session.id)
    }

    private fun handleSendMessage(text: String) {
        if (text.isBlank() || currentState.isLoading) return
        val model = currentState.selectedModel
        val session = currentState.selectedSession

        launch {
            val sessionId = session?.id ?: run {
                val newSession = createSession("New Chat", model)
                setState { copy(selectedSession = newSession) }
                observeMessagesFor(newSession.id)
                newSession.id
            }

            setState { copy(isLoading = true, inputText = "") }

            sendMessage(sessionId, text, model)
                .onSuccess {
                    setState { copy(isLoading = false) }
                }
                .onFailure { error ->
                    setState { copy(isLoading = false, inputText = text) }
                    if (error.message?.contains("credit", ignoreCase = true) == true) {
                        setEffect { ChatEffect.ShowCreditExhausted }
                    } else {
                        setEffect { ChatEffect.ShowError(error.message ?: "Failed to send message") }
                    }
                }
        }
    }

    private suspend fun handleCreateSession(title: String) {
        val model = currentState.selectedModel
        val session = createSession(title, model)
        setState { copy(selectedSession = session) }
        observeMessagesFor(session.id)
    }

    private fun handleDeleteSession(sessionId: Long) {
        launch {
            deleteSession(sessionId)
            if (currentState.selectedSession?.id == sessionId) {
                val remaining = currentState.sessions.filter { it.id != sessionId }
                if (remaining.isNotEmpty()) {
                    handleSelectSession(remaining.first())
                } else {
                    setState { copy(selectedSession = null, messages = emptyList()) }
                    messagesJob?.cancel()
                }
            }
        }
    }

    private fun observeMessagesFor(sessionId: Long) {
        messagesJob?.cancel()
        messagesJob = chatRepo.observeMessages(sessionId).onEach { messages ->
            setState { copy(messages = messages) }
        }.launchIn(viewModelScope)
    }
}
