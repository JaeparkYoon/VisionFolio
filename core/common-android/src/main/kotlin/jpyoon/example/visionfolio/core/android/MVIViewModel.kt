package jpyoon.example.visionfolio.core.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class MVIViewModel<I : ViewIntent, S : ViewState, E : ViewEffect> : ViewModel() {

    abstract fun createInitialState(): S

    private val initialState: S by lazy { createInitialState() }

    private val _intent: MutableSharedFlow<I> = MutableSharedFlow()
    private val intent = _intent.asSharedFlow()

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    private val _effect: Channel<E> = Channel()
    val effect = _effect.receiveAsFlow()

    protected val currentState: S
        get() = _state.value

    private val _jobs: MutableMap<String, Job> = mutableMapOf()

    private val _errorHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    init {
        subscribeIntent()
    }

    private fun subscribeIntent() {
        launch {
            intent.collect {
                try {
                    processIntent(it)
                } catch (exception: Throwable) {
                    handleException(exception)
                }
            }
        }
    }

    /**
     * Intent를 처리합니다.
     */
    abstract suspend fun processIntent(intent: I)

    /**
     * 새로운 Intent를 방출합니다.
     */
    fun dispatch(intent: I) {
        launch { _intent.emit(intent) }
    }

    /**
     * UI 상태를 업데이트합니다.
     */
    protected fun setState(reduce: S.() -> S) {
        val newState = currentState.reduce()
        _state.value = newState
    }

    /**
     * View Effect를 발생시킵니다.
     */
    protected fun setEffect(builder: () -> E) {
        val newEffect = builder()
        launch { _effect.send(newEffect) }
    }

    /**
     * Coroutine Task 실행 중 감지되지 않은 예외를 처리합니다.
     */
    protected open fun handleException(exception: Throwable) {
        exception.printStackTrace()
    }

    /**
     * Coroutine launch 빌더를 통해 Job을 생성합니다.
     */
    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        errorHandler: CoroutineExceptionHandler = _errorHandler,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = (viewModelScope + errorHandler).launch(context, start, block)

    /**
     * Key 기준으로 마지막 Job만 실행이 유지되도록 합니다.
     */
    protected fun launchLatest(
        key: String,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        errorHandler: CoroutineExceptionHandler = _errorHandler,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = launch(context, start, errorHandler) {
        cancelLaunchLatestJob(key)
        launch(context, start, errorHandler, block).also {
            _jobs[key] = it
        }.join()
    }

    protected suspend fun cancelLaunchLatestJob(key: String) = _jobs[key]?.cancelAndJoin()

    /**
     * Coroutine async 빌더를 통해 Deferred<T>를 생성합니다.
     */
    protected fun <T> async(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T,
    ): Deferred<T> = viewModelScope.async(context, start, block)
}
