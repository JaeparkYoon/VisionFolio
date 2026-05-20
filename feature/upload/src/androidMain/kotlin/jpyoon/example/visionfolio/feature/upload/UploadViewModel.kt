package jpyoon.example.visionfolio.feature.upload

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import jpyoon.example.visionfolio.core.common.MVIViewModel
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.ParsedField
import jpyoon.example.visionfolio.domain.model.ParsedHolding
import jpyoon.example.visionfolio.domain.model.ScreenshotRef
import jpyoon.example.visionfolio.domain.model.UploadStage
import jpyoon.example.visionfolio.data.ai.ScreenshotParseResultStore
import jpyoon.example.visionfolio.data.ai.ScreenshotParseSession
import jpyoon.example.visionfolio.data.fingerprint.ScreenshotFingerprinter
import jpyoon.example.visionfolio.core.repository.api.ImportSourceRepository
import jpyoon.example.visionfolio.domain.model.UploadResult
import jpyoon.example.visionfolio.domain.usecase.CommitParsedHoldings
import jpyoon.example.visionfolio.feature.upload.service.ScreenshotParseService
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class UploadViewModel(
    private val context: Context,
    private val commit: CommitParsedHoldings,
    private val fingerprinter: ScreenshotFingerprinter,
    private val importSourceRepo: ImportSourceRepository,
    private val session: ScreenshotParseSession,
    private val resultStore: ScreenshotParseResultStore,
    private val savedStateHandle: SavedStateHandle,
) : MVIViewModel<UploadIntent, UploadState, UploadEffect>() {

    override fun createInitialState(): UploadState = UploadState()

    init {
        // createInitialState() 는 부모의 lazy 초기화 시점에 호출돼 primary constructor 프로퍼티에
        // 접근하면 NPE 가 날 수 있어, 영속화된 state 는 여기서 복원한다.
        val restored = restoreFromSavedState()
        setState { restored }

        viewModelScope.launch {
            // 1) Service 가 파싱을 끝내고 프로세스가 정리된 케이스: 디스크에서 결과를 회수.
            val persisted = resultStore.consume()
            if (persisted != null) {
                Log.d(TAG, "init: 영속화된 파싱 결과 복원 — ${persisted.parsed.size}건")
                session.reset() // 메모리 세션에도 같은 결과가 있을 수 있어 중복 처리 방지
                applyParsedResult(persisted)
                return@launch
            }

            // 2) 프로세스가 파싱 도중 죽고 재생성된 경우 자동 재파싱.
            if ((restored.stage == UploadStage.DOWNLOADING_MODEL || restored.stage == UploadStage.PARSING)
                && restored.shots.isNotEmpty()
                && session.state.value is ScreenshotParseSession.State.Idle
            ) {
                Log.d(TAG, "init: 파싱 중 프로세스 재생성 감지 → 자동 재파싱")
                startParse()
            }
        }

        // state 변화를 SavedStateHandle 에 반영해 다음 재생성에 대비.
        viewModelScope.launch {
            state.collect { s ->
                savedStateHandle[KEY_SHOT_URIS] = ArrayList(s.shots.map { it.uri })
                savedStateHandle[KEY_SHOT_SIZES] = s.shots.map { it.sizeBytes }.toLongArray()
                savedStateHandle[KEY_STAGE] = s.stage.name
            }
        }

        // Service 에서 내려오는 세션 상태를 UI state 로 반영.
        viewModelScope.launch {
            session.state.collect { onSessionState(it) }
        }
    }

    private fun restoreFromSavedState(): UploadState {
        val uris = savedStateHandle.get<ArrayList<String>>(KEY_SHOT_URIS).orEmpty()
        val sizes = savedStateHandle.get<LongArray>(KEY_SHOT_SIZES) ?: LongArray(0)
        val shots = uris.mapIndexed { i, uri ->
            ScreenshotRef(uri = uri, sizeBytes = sizes.getOrNull(i) ?: 0L)
        }
        val savedStage = savedStateHandle.get<String>(KEY_STAGE)
            ?.let { runCatching { UploadStage.valueOf(it) }.getOrNull() }
            ?: UploadStage.PICK
        // REVIEW/DONE 은 parsed 결과를 영속화하지 않아 그대로 복원 불가 → PICK 으로 폴백.
        val stage = when (savedStage) {
            UploadStage.REVIEW, UploadStage.DONE -> UploadStage.PICK
            else -> savedStage
        }
        return UploadState(stage = stage, shots = shots)
    }

    override suspend fun processIntent(intent: UploadIntent) {
        Log.d(TAG, "processIntent: $intent (현재 stage=${currentState.stage})")
        when (intent) {
            is UploadIntent.AddShots -> setState { copy(shots = shots + intent.refs) }

            is UploadIntent.RemoveShot -> setState {
                copy(shots = shots.filterIndexed { idx, _ -> idx != intent.index })
            }

            UploadIntent.StartParse -> startParse()

            UploadIntent.CancelParse -> cancelParse()

            is UploadIntent.TogglePick -> setState {
                copy(parsed = parsed.mapIndexedToggle(intent.index))
            }

            is UploadIntent.EditField -> setState {
                copy(parsed = parsed.mapIndexedEdit(intent.index, intent.field, intent.value))
            }

            UploadIntent.BackToPick -> setState { copy(stage = UploadStage.PICK) }

            UploadIntent.ConfirmAdd -> confirmAdd()

            UploadIntent.Dismiss -> setEffect { UploadEffect.Close }
        }
    }

    private fun startParse() {
        val snapshot = currentState
        if (snapshot.shots.isEmpty()) {
            Log.w(TAG, "startParse: shots 가 비어 있어 중단")
            return
        }
        Log.d(TAG, "startParse: Foreground Service 에 ${snapshot.shots.size}장 위임")
        setState {
            copy(
                stage = UploadStage.DOWNLOADING_MODEL,
                parseError = null,
                modelDownloadProgress = 0f,
                quotaExceeded = false,
                quotaRetryAttempt = 0,
                quotaMaxRetries = 0,
            )
        }
        ScreenshotParseService.start(context, snapshot.shots)
    }

    private fun cancelParse() {
        Log.d(TAG, "cancelParse: Service 취소 요청")
        ScreenshotParseService.cancel(context)
        // 즉각적인 UI 반응. Service 가 markCancelled 하면 observer 가 동일 상태를 재확인한다.
        setState { copy(stage = UploadStage.PICK) }
    }

    private suspend fun onSessionState(state: ScreenshotParseSession.State) {
        when (state) {
            ScreenshotParseSession.State.Idle -> Unit

            is ScreenshotParseSession.State.Running -> {
                setState {
                    copy(
                        stage = if (state.modelDownloadProgress >= 1f) UploadStage.PARSING else UploadStage.DOWNLOADING_MODEL,
                        modelDownloadProgress = state.modelDownloadProgress,
                        quotaExceeded = state.quotaRetry != null,
                        quotaRetryAttempt = state.quotaRetry?.attempt ?: 0,
                        quotaMaxRetries = state.quotaRetry?.max ?: 0,
                    )
                }
            }

            is ScreenshotParseSession.State.Succeeded -> {
                Log.d(TAG, "onSessionState: 파싱 성공 — parsed=${state.result.parsed.size}건")
                applyParsedResult(state.result)
                session.reset()
                resultStore.clear() // 메모리에서 이미 처리했으므로 디스크 중복 소비 방지
            }

            is ScreenshotParseSession.State.Failed -> {
                Log.e(TAG, "onSessionState: 파싱 실패", state.error)
                setState { copy(stage = UploadStage.PICK, parseError = state.error.message) }
                setEffect { UploadEffect.ShowError(state.error.message ?: "파싱 실패") }
                session.reset()
                resultStore.clear()
            }

            ScreenshotParseSession.State.Cancelled -> {
                Log.d(TAG, "onSessionState: 사용자 취소")
                setState { copy(stage = UploadStage.PICK, parseError = null) }
                session.reset()
                resultStore.clear()
            }
        }
    }

    private fun confirmAdd() {
        val state = currentState
        val approved = state.parsed.filter { it.selected }
        if (approved.isEmpty()) {
            setEffect { UploadEffect.ShowError("추가할 항목을 선택해 주세요.") }
            return
        }
        Log.d(TAG, "confirmAdd: ${approved.size}건 커밋 시작 (source=${state.resolvedSource})")
        viewModelScope.launch {
            val added = commit(approved, state.resolvedSource)
            Log.d(TAG, "confirmAdd: 커밋 완료 → added=$added, Close effect 발행")
            setState { copy(stage = UploadStage.DONE) }
            setEffect { UploadEffect.CommittedAdds(added) }
            setEffect { UploadEffect.Close }
        }
    }

    private suspend fun applyParsedResult(result: UploadResult) {
        if (result.parsed.isEmpty()) {
            setState { copy(stage = UploadStage.PICK, parseError = "인식된 항목이 없습니다") }
            setEffect { UploadEffect.ShowError("스크린샷에서 보유 종목을 찾지 못했습니다. 증권사 앱의 '보유 종목' 화면을 다시 시도해 주세요.") }
        } else {
            val source = resolveSource(currentState.shots)
            setState {
                copy(
                    stage = UploadStage.REVIEW,
                    parsed = result.parsed,
                    elapsedMs = result.elapsedMs,
                    resolvedSource = source,
                )
            }
        }
    }

    private suspend fun resolveSource(shots: List<ScreenshotRef>): String {
        val firstUri = shots.firstOrNull()?.uri ?: return ""
        return try {
            val fp = fingerprinter.compute(firstUri)
            if (fp == 0L) return ""
            importSourceRepo.resolveLabel(fp)
        } catch (e: Exception) {
            Log.w(TAG, "resolveSource: fingerprint 실패", e)
            ""
        }
    }

    companion object {
        private const val TAG = "UploadVM"
        private const val KEY_SHOT_URIS = "upload.shotUris"
        private const val KEY_SHOT_SIZES = "upload.shotSizes"
        private const val KEY_STAGE = "upload.stage"
    }
}

private fun List<ParsedHolding>.mapIndexedToggle(target: Int): List<ParsedHolding> =
    mapIndexed { idx, row -> if (idx == target) row.copy(selected = !row.selected) else row }

private fun List<ParsedHolding>.mapIndexedEdit(
    target: Int,
    field: ParsedField,
    value: String,
): List<ParsedHolding> = mapIndexed { idx, row ->
    if (idx != target) return@mapIndexed row
    when (field) {
        ParsedField.NAME -> row.copy(name = value)
        ParsedField.CODE -> row.copy(code = value)
        ParsedField.QUANTITY -> row.copy(quantity = value.toDoubleOrNull() ?: row.quantity)
        ParsedField.AVG_PRICE -> row.copy(avgPrice = value.toDoubleOrNull() ?: row.avgPrice)
        ParsedField.CURRENT_PRICE -> row.copy(currentPrice = value.toDoubleOrNull() ?: row.currentPrice)
        ParsedField.CATEGORY -> row.copy(
            category = runCatching { AssetCategory.valueOf(value) }.getOrDefault(row.category),
        )
        ParsedField.MATURITY_DATE -> row.copy(maturityDate = value.ifBlank { null })
    }
}
