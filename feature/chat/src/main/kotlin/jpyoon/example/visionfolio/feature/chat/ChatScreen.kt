package jpyoon.example.visionfolio.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.VfChip
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.chat.ChatMessage
import jpyoon.example.visionfolio.domain.model.chat.ChatRole
import jpyoon.example.visionfolio.domain.model.chat.ChatSession
import jpyoon.example.visionfolio.domain.model.chat.GeminiModel

@Composable
fun ChatScreen(
    state: ChatState,
    onIntent: (ChatIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.activeSessionId == null) {
        SessionListScreen(
            sessions = state.sessions,
            onSelect = { onIntent(ChatIntent.SelectSession(it)) },
            onCreate = { onIntent(ChatIntent.CreateSession) },
            onDelete = { onIntent(ChatIntent.DeleteSession(it)) },
            onBack = onBack,
            modifier = modifier,
        )
    } else {
        ConversationScreen(
            state = state,
            onIntent = onIntent,
            onBack = { onIntent(ChatIntent.BackToSessionList) },
            modifier = modifier,
        )
    }
}

@Composable
private fun SessionListScreen(
    sessions: List<ChatSession>,
    onSelect: (String) -> Unit,
    onCreate: () -> Unit,
    onDelete: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VfColors.BgDefault),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, tint = VfColors.InkPrimary)
            }
            Text("AI 채팅", style = VfTypography.TitleScreen, color = VfColors.InkPrimary)
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onCreate) {
                Icon(Icons.Outlined.Add, contentDescription = "새 대화", tint = VfColors.InkPrimary)
            }
        }

        if (sessions.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("대화가 없습니다.\n새 대화를 시작해보세요.", style = VfTypography.HeadingSection, color = VfColors.InkMuted)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(sessions, key = { it.id }) { session ->
                    SessionRow(
                        session = session,
                        onClick = { onSelect(session.id) },
                        onDelete = { onDelete(session.id) },
                    )
                    HorizontalDivider(color = VfColors.BgAlt)
                }
            }
        }
    }
}

@Composable
private fun SessionRow(
    session: ChatSession,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(session.title, style = VfTypography.HeadingSection, color = VfColors.InkPrimary)
            Text(
                "${session.model.label} · ${formatTime(session.updatedAt)}",
                style = VfTypography.MetaSub,
                color = VfColors.InkTertiary,
            )
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
            Icon(Icons.Outlined.Delete, contentDescription = "삭제", tint = VfColors.Down, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun ConversationScreen(
    state: ChatState,
    onIntent: (ChatIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VfColors.BgDefault)
            .imePadding(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, tint = VfColors.InkPrimary)
            }
            Text("AI 채팅", style = VfTypography.TitleScreen, color = VfColors.InkPrimary)
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(end = 12.dp)) {
                GeminiModel.entries.forEach { model ->
                    VfChip(
                        text = model.label,
                        selected = state.selectedModel == model,
                        onClick = { onIntent(ChatIntent.SelectModel(model)) },
                    )
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.messages, key = { it.id }) { message ->
                MessageBubble(message)
            }
            if (state.streamingText.isNotEmpty()) {
                item(key = "streaming") {
                    MessageBubble(
                        ChatMessage(
                            id = "streaming",
                            sessionId = "",
                            role = ChatRole.ASSISTANT,
                            content = state.streamingText,
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = state.input,
                onValueChange = { onIntent(ChatIntent.SetInput(it)) },
                placeholder = { Text("메시지를 입력하세요") },
                singleLine = false,
                maxLines = 4,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = { onIntent(ChatIntent.SendMessage) },
                enabled = state.input.isNotBlank() && !state.isSending,
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Send,
                    contentDescription = "전송",
                    tint = if (state.input.isNotBlank() && !state.isSending) VfColors.InkPrimary else VfColors.InkMuted,
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == ChatRole.USER
    val bgColor = if (isUser) VfColors.InkPrimary else VfColors.BgAlt
    val textColor = if (isUser) VfColors.BgDefault else VfColors.InkPrimary
    val alignment = if (isUser) Alignment.End else Alignment.Start

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(bgColor)
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Text(
                text = message.content,
                style = VfTypography.BodyDefault,
                color = textColor,
            )
        }
    }
}

private fun formatTime(millis: Long): String {
    val sdf = java.text.SimpleDateFormat("MM/dd HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(millis))
}
