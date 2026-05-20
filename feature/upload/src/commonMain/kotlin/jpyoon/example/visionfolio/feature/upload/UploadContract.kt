package jpyoon.example.visionfolio.feature.upload

import jpyoon.example.visionfolio.core.common.ViewIntent
import jpyoon.example.visionfolio.core.common.ViewEffect
import jpyoon.example.visionfolio.core.common.ViewState
import jpyoon.example.visionfolio.domain.model.ParsedField
import jpyoon.example.visionfolio.domain.model.ParsedHolding
import jpyoon.example.visionfolio.domain.model.ScreenshotRef
import jpyoon.example.visionfolio.domain.model.UploadStage

data class UploadState(
    val stage: UploadStage = UploadStage.PICK,
    val shots: List<ScreenshotRef> = emptyList(),
    val parsed: List<ParsedHolding> = emptyList(),
    val elapsedMs: Long = 0,
    val parseError: String? = null,
    val modelDownloadProgress: Float = 0f,
    val quotaExceeded: Boolean = false,
    val quotaRetryAttempt: Int = 0,
    val quotaMaxRetries: Int = 0,
    val resolvedSource: String = "",
) : ViewState

val UploadState.canStartParse: Boolean
    get() = stage == UploadStage.PICK && shots.isNotEmpty()

val UploadState.selectedCount: Int
    get() = parsed.count { it.selected }

sealed interface UploadIntent : ViewIntent {
    data class AddShots(val refs: List<ScreenshotRef>) : UploadIntent
    data class RemoveShot(val index: Int) : UploadIntent
    object StartParse : UploadIntent
    object CancelParse : UploadIntent
    data class TogglePick(val index: Int) : UploadIntent
    data class EditField(val index: Int, val field: ParsedField, val value: String) : UploadIntent
    object BackToPick : UploadIntent
    object ConfirmAdd : UploadIntent
    object Dismiss : UploadIntent
}

sealed interface UploadEffect : ViewEffect {
    object Close : UploadEffect
    data class CommittedAdds(val count: Int) : UploadEffect
    data class ShowError(val message: String) : UploadEffect
}
