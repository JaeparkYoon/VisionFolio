package jpyoon.example.visionfolio.feature.returns

import jpyoon.example.visionfolio.core.android.ViewEffect
import jpyoon.example.visionfolio.core.android.ViewIntent
import jpyoon.example.visionfolio.core.android.ViewState
import jpyoon.example.visionfolio.domain.model.ReturnCategory
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import jpyoon.example.visionfolio.domain.model.YtdSummary

data class ReturnsState(
    val year: Int = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR),
    val entries: List<ReturnEntry> = emptyList(),
    val ytdSummary: YtdSummary? = null,
    val isEditorOpen: Boolean = false,
    val editorDraft: EditorDraft = EditorDraft(),
) : ViewState

data class EditorDraft(
    val editingId: String? = null,
    val month: Int = java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1,
    val category: ReturnCategory = ReturnCategory.SALARY,
    val amount: String = "",
    val note: String = "",
)

sealed interface ReturnsIntent : ViewIntent {
    data class SetYear(val year: Int) : ReturnsIntent
    object OpenEditor : ReturnsIntent
    data class StartEdit(val entry: ReturnEntry) : ReturnsIntent
    object DismissEditor : ReturnsIntent
    data class SetEditorMonth(val month: Int) : ReturnsIntent
    data class SetEditorCategory(val category: ReturnCategory) : ReturnsIntent
    data class SetEditorAmount(val amount: String) : ReturnsIntent
    data class SetEditorNote(val note: String) : ReturnsIntent
    object SaveEntry : ReturnsIntent
    data class DeleteEntry(val id: String) : ReturnsIntent
}

sealed interface ReturnsEffect : ViewEffect {
    data class ShowError(val message: String) : ReturnsEffect
}
