package jpyoon.example.visionfolio.feature.returns

import jpyoon.example.visionfolio.core.common.ViewEffect
import jpyoon.example.visionfolio.core.common.ViewIntent
import jpyoon.example.visionfolio.core.common.ViewState
import jpyoon.example.visionfolio.domain.model.ReturnCategory
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import jpyoon.example.visionfolio.domain.model.YtdSummary

data class ReturnsState(
    val entries: List<ReturnEntry> = emptyList(),
    val ytdSummary: YtdSummary? = null,
    val selectedYear: String? = null,
    val filter: ReturnCategory? = null,
    val editorEntry: ReturnEntry? = null,
    val isEditorVisible: Boolean = false,
) : ViewState {
    val filteredEntries: List<ReturnEntry>
        get() = filter?.let { f -> entries.filter { it.category == f } } ?: entries
}

sealed interface ReturnsIntent : ViewIntent {
    data class SelectYear(val year: String) : ReturnsIntent
    data class FilterCategory(val category: ReturnCategory?) : ReturnsIntent
    data class AddEntry(val entry: ReturnEntry) : ReturnsIntent
    data class EditEntry(val entry: ReturnEntry) : ReturnsIntent
    data class DeleteEntry(val id: Long) : ReturnsIntent
    data class SetBaseline(val amount: Long) : ReturnsIntent
    object OpenEditor : ReturnsIntent
    object CloseEditor : ReturnsIntent
    data class SaveEntry(val entry: ReturnEntry) : ReturnsIntent
    object NavigateBack : ReturnsIntent
}

sealed interface ReturnsEffect : ViewEffect {
    object NavigateBack : ReturnsEffect
    data class ShowError(val message: String) : ReturnsEffect
    object ShowSaved : ReturnsEffect
}
