package jpyoon.example.visionfolio.feature.returns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import jpyoon.example.visionfolio.designsystem.component.VfCard
import jpyoon.example.visionfolio.designsystem.component.VfChip
import jpyoon.example.visionfolio.designsystem.foundation.VfColors
import jpyoon.example.visionfolio.designsystem.foundation.VfTypography
import jpyoon.example.visionfolio.domain.model.ReturnCategory
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import jpyoon.example.visionfolio.domain.model.YtdSummary
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReturnsScreen(
    state: ReturnsState,
    onIntent: (ReturnsIntent) -> Unit,
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
            Column(modifier = Modifier.padding(start = 4.dp)) {
                Text("수익 관리", style = VfTypography.TitleScreen, color = VfColors.InkPrimary)
                Text("연간 수익을 관리하세요", style = VfTypography.MetaSub, color = VfColors.InkTertiary)
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { onIntent(ReturnsIntent.OpenEditor) }) {
                Icon(Icons.Outlined.Add, contentDescription = "추가", tint = VfColors.InkPrimary)
            }
        }

        // Year selector
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            (currentYear downTo currentYear - 3).forEach { year ->
                VfChip(
                    text = "${year}년",
                    selected = state.year == year,
                    onClick = { onIntent(ReturnsIntent.SetYear(year)) },
                )
            }
        }

        // YTD Summary
        state.ytdSummary?.let { summary ->
            YtdSummaryCard(
                summary = summary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }

        Spacer(Modifier.height(8.dp))

        // Entries list
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.entries, key = { it.id }) { entry ->
                ReturnEntryRow(
                    entry = entry,
                    onEdit = { onIntent(ReturnsIntent.StartEdit(entry)) },
                    onDelete = { onIntent(ReturnsIntent.DeleteEntry(entry.id)) },
                )
                HorizontalDivider(color = VfColors.BgAlt)
            }
        }
    }

    if (state.isEditorOpen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { onIntent(ReturnsIntent.DismissEditor) },
            sheetState = sheetState,
            containerColor = VfColors.Card,
        ) {
            EntryEditorContent(
                draft = state.editorDraft,
                onIntent = onIntent,
            )
        }
    }
}

@Composable
private fun YtdSummaryCard(
    summary: YtdSummary,
    modifier: Modifier = Modifier,
) {
    val nf = NumberFormat.getNumberInstance(Locale.getDefault())

    VfCard(modifier = modifier) {
        Text("${summary.year}년 요약", style = VfTypography.HeadingSection, color = VfColors.InkPrimary)
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SummaryItem("총 수입", "₩${nf.format(summary.totalIncome)}")
            SummaryItem("총 투자", "₩${nf.format(summary.totalInvestment)}")
            SummaryItem("저축률", "%.1f%%".format(summary.savingsRate))
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = VfTypography.MetaSub, color = VfColors.InkTertiary)
        Text(value, style = VfTypography.HeadingSection, color = VfColors.InkPrimary)
    }
}

@Composable
private fun ReturnEntryRow(
    entry: ReturnEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val nf = NumberFormat.getNumberInstance(Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${entry.month}월 · ${entry.category.displayLabel}",
                style = VfTypography.HeadingSection,
                color = VfColors.InkPrimary,
            )
            if (entry.note.isNotBlank()) {
                Text(entry.note, style = VfTypography.MetaSub, color = VfColors.InkTertiary, maxLines = 1)
            }
        }
        Text(
            "₩${nf.format(entry.amount)}",
            style = VfTypography.BodyDefault,
            color = VfColors.InkPrimary,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(start = 8.dp)) {
            IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Outlined.Edit, contentDescription = "편집", tint = VfColors.InkSecondary, modifier = Modifier.size(18.dp))
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                Icon(Icons.Outlined.Delete, contentDescription = "삭제", tint = VfColors.Down, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun EntryEditorContent(
    draft: EditorDraft,
    onIntent: (ReturnsIntent) -> Unit,
) {
    val isEdit = draft.editingId != null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
    ) {
        Text(
            if (isEdit) "수익 수정" else "수익 추가",
            style = VfTypography.TitleScreen,
            color = VfColors.InkPrimary,
        )

        Spacer(Modifier.height(16.dp))
        Text("월", style = VfTypography.LabelCaps, color = VfColors.InkTertiary)
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            (1..12).forEach { month ->
                VfChip(
                    text = "${month}월",
                    selected = draft.month == month,
                    onClick = { onIntent(ReturnsIntent.SetEditorMonth(month)) },
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Text("카테고리", style = VfTypography.LabelCaps, color = VfColors.InkTertiary)
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            ReturnCategory.entries.forEach { cat ->
                VfChip(
                    text = cat.displayLabel,
                    selected = draft.category == cat,
                    onClick = { onIntent(ReturnsIntent.SetEditorCategory(cat)) },
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = draft.amount,
            onValueChange = { onIntent(ReturnsIntent.SetEditorAmount(it)) },
            label = { Text("금액 (원)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = draft.note,
            onValueChange = { onIntent(ReturnsIntent.SetEditorNote(it)) },
            label = { Text("메모 (선택)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(20.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = { onIntent(ReturnsIntent.DismissEditor) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VfColors.BgAlt,
                    contentColor = VfColors.InkSecondary,
                ),
                modifier = Modifier.weight(1f),
            ) { Text("취소") }

            Button(
                onClick = { onIntent(ReturnsIntent.SaveEntry) },
                enabled = draft.amount.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VfColors.InkPrimary,
                    contentColor = VfColors.BgDefault,
                ),
                modifier = Modifier.weight(1f),
            ) { Text(if (isEdit) "저장" else "추가") }
        }

        Spacer(Modifier.height(16.dp))
    }
}

private val ReturnCategory.displayLabel: String
    get() = when (this) {
        ReturnCategory.SALARY -> "급여"
        ReturnCategory.INVESTMENT -> "투자"
        ReturnCategory.SIDE_INCOME -> "부수입"
        ReturnCategory.BONUS -> "보너스"
        ReturnCategory.OTHER -> "기타"
    }
