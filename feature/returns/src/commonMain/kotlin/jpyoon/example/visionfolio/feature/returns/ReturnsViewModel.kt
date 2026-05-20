package jpyoon.example.visionfolio.feature.returns

import jpyoon.example.visionfolio.core.common.MVIViewModel
import jpyoon.example.visionfolio.core.repository.api.AppPrefsRepository
import jpyoon.example.visionfolio.domain.model.ReturnEntry
import jpyoon.example.visionfolio.domain.usecase.returns.DeleteReturnEntryUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.GetYtdSummaryUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.ObserveReturnsUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.UpsertReturnEntryUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject

class ReturnsViewModel @Inject constructor(
    private val observeReturns: ObserveReturnsUseCase,
    private val upsertEntry: UpsertReturnEntryUseCase,
    private val deleteEntry: DeleteReturnEntryUseCase,
    private val getYtdSummary: GetYtdSummaryUseCase,
    private val appPrefsRepo: AppPrefsRepository,
) : MVIViewModel<ReturnsIntent, ReturnsState, ReturnsEffect>() {

    private val currentYear: String
        get() = kotlinx.datetime.Clock.System.now()
            .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
            .year.toString()

    override fun createInitialState(): ReturnsState = ReturnsState(selectedYear = currentYear)

    private var yearJob: Job? = null

    init {
        subscribeYear(currentYear)
    }

    private fun subscribeYear(year: String) {
        yearJob?.cancel()
        val yearInt = year.toIntOrNull() ?: return

        yearJob = observeReturns.byYear(yearInt).onEach { entries ->
            setState { copy(entries = entries, selectedYear = year) }
        }.launchIn(viewModelScope)

        launch {
            runCatching { getYtdSummary(yearInt) }
                .onSuccess { summary -> setState { copy(ytdSummary = summary) } }
                .onFailure { /* summary unavailable */ }
        }
    }

    override suspend fun processIntent(intent: ReturnsIntent) {
        when (intent) {
            is ReturnsIntent.SelectYear -> {
                setState { copy(selectedYear = intent.year) }
                subscribeYear(intent.year)
            }
            is ReturnsIntent.FilterCategory -> setState { copy(filter = intent.category) }
            is ReturnsIntent.AddEntry -> handleUpsertEntry(intent.entry)
            is ReturnsIntent.EditEntry -> setState { copy(editorEntry = intent.entry, isEditorVisible = true) }
            is ReturnsIntent.DeleteEntry -> handleDeleteEntry(intent.id)
            is ReturnsIntent.SetBaseline -> handleSetBaseline(intent.amount)
            ReturnsIntent.OpenEditor -> setState { copy(isEditorVisible = true, editorEntry = null) }
            ReturnsIntent.CloseEditor -> setState { copy(isEditorVisible = false, editorEntry = null) }
            is ReturnsIntent.SaveEntry -> handleUpsertEntry(intent.entry)
            ReturnsIntent.NavigateBack -> setEffect { ReturnsEffect.NavigateBack }
        }
    }

    private fun handleUpsertEntry(entry: ReturnEntry) {
        launch {
            runCatching { upsertEntry(entry) }
                .onSuccess {
                    setState { copy(isEditorVisible = false, editorEntry = null) }
                    setEffect { ReturnsEffect.ShowSaved }
                }
                .onFailure { error ->
                    setEffect { ReturnsEffect.ShowError(error.message ?: "Save failed") }
                }
        }
    }

    private fun handleDeleteEntry(id: Long) {
        launch {
            runCatching { deleteEntry(id) }
                .onFailure { error ->
                    setEffect { ReturnsEffect.ShowError(error.message ?: "Delete failed") }
                }
        }
    }

    private fun handleSetBaseline(amount: Long) {
        val year = currentState.selectedYear?.toIntOrNull() ?: return
        launch {
            runCatching { appPrefsRepo.setReturnBaseline(year, amount) }
                .onFailure { error ->
                    setEffect { ReturnsEffect.ShowError(error.message ?: "Failed to set baseline") }
                }
        }
    }
}
