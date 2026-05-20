package jpyoon.example.visionfolio.data.ai

import jpyoon.example.visionfolio.domain.model.UploadResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

/**
 * Foreground Service 로 위임된 파싱 작업의 진행/결과를 ViewModel 에 전달하는 싱글톤 브릿지.
 * Service 는 여기에 state 를 써주고, ViewModel 은 구독해 UI 를 갱신한다.
 */
@AppSingleton
class ScreenshotParseSession @Inject constructor() {

    sealed interface State {
        data object Idle : State
        data class Running(
            val modelDownloadProgress: Float = 0f,
            val quotaRetry: QuotaRetry? = null,
        ) : State
        data class Succeeded(val result: UploadResult) : State
        data class Failed(val error: Throwable) : State
        data object Cancelled : State
    }

    data class QuotaRetry(val attempt: Int, val max: Int)

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    fun markRunning(modelDownloadProgress: Float = 0f, quotaRetry: QuotaRetry? = null) {
        _state.value = State.Running(modelDownloadProgress, quotaRetry)
    }

    fun markSucceeded(result: UploadResult) {
        _state.value = State.Succeeded(result)
    }

    fun markFailed(error: Throwable) {
        _state.value = State.Failed(error)
    }

    fun markCancelled() {
        _state.value = State.Cancelled
    }

    fun reset() {
        _state.value = State.Idle
    }
}
