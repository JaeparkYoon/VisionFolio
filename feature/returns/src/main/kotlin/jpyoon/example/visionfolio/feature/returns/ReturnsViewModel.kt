package jpyoon.example.visionfolio.feature.returns

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpyoon.example.visionfolio.core.android.MVIViewModel
import jpyoon.example.visionfolio.data.repository.ReturnEntryRepository
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ReturnsViewModel @Inject constructor(
    private val returnEntryRepo: ReturnEntryRepository,
) : MVIViewModel<ReturnsIntent, ReturnsState, ReturnsEffect>() {

    override fun createInitialState(): ReturnsState = ReturnsState()

    private var entriesJob: Job? = null

    init {
        observeYear(state.value.year)
    }

    private fun observeYear(year: Int) {
        entriesJob?.cancel()
        entriesJob = returnEntryRepo.observeByYear(year).onEach { entries ->
            setState { copy(entries = entries) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val summary = returnEntryRepo.getYtdSummary(year)
            setState { copy(ytdSummary = summary) }
        }
    }

    override suspend fun processIntent(intent: ReturnsIntent) {
        when (intent) {
            is ReturnsIntent.SetYear -> {
                setState { copy(year = intent.year) }
                observeYear(intent.year)
            }
            ReturnsIntent.OpenEditor -> {
                setState { copy(isEditorOpen = true, editorDraft = EditorDraft()) }
            }
            is ReturnsIntent.StartEdit -> {
                setState {
                    copy(
                        isEditorOpen = true,
                        editorDraft = EditorDraft(
                            editingId = intent.entry.id,
                            month = intent.entry.month,
                            category = intent.entry.category,
                            amount = intent.entry.amount.toString(),
                            note = intent.entry.note,
                        ),
                    )
                }
            }
            ReturnsIntent.DismissEditor -> {
                setState { copy(isEditorOpen = false) }
            }
            is ReturnsIntent.SetEditorMonth -> setState { copy(editorDraft = editorDraft.copy(month = intent.month)) }
            is ReturnsIntent.SetEditorCategory -> setState { copy(editorDraft = editorDraft.copy(category = intent.category)) }
            is ReturnsIntent.SetEditorAmount -> setState { copy(editorDraft = editorDraft.copy(amount = intent.amount)) }
            is ReturnsIntent.SetEditorNote -> setState { copy(editorDraft = editorDraft.copy(note = intent.note)) }
            ReturnsIntent.SaveEntry -> saveEntry()
            is ReturnsIntent.DeleteEntry -> {
                returnEntryRepo.delete(intent.id)
                refreshSummary()
            }
        }
    }

    private suspend fun saveEntry() {
        val draft = state.value.editorDraft
        val amount = draft.amount.toLongOrNull()
        if (amount == null || amount <= 0) {
            setEffect { ReturnsEffect.ShowError("금액을 올바르게 입력해주세요") }
            return
        }

        val entry = ReturnEntry(
            id = draft.editingId ?: UUID.randomUUID().toString(),
            year = state.value.year,
            month = draft.month,
            category = draft.category,
            amount = amount,
            note = draft.note,
        )

        returnEntryRepo.upsert(entry)
        setState { copy(isEditorOpen = false) }
        refreshSummary()
    }

    private suspend fun refreshSummary() {
        val summary = returnEntryRepo.getYtdSummary(state.value.year)
        setState { copy(ytdSummary = summary) }
    }
}
