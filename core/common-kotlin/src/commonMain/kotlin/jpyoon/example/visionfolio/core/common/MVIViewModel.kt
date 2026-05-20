package jpyoon.example.visionfolio.core.common

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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class MVIViewModel<I : ViewIntent, S : ViewState, E : ViewEffect> : PlatformViewModel() {

    abstract fun createInitialState(): S

    private val initialState: S by lazy { createInitialState() }

    private val _intent: MutableSharedFlow<I> = MutableSharedFlow()
    private val intent = _intent.asSharedFlow()

    private val _state: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
    val state: StateFlow<S> by lazy { _state.asStateFlow() }

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

    abstract suspend fun processIntent(intent: I)

    fun dispatch(intent: I) {
        launch { _intent.emit(intent) }
    }

    protected fun setState(reduce: S.() -> S) {
        val newState = currentState.reduce()
        _state.value = newState
    }

    protected fun setEffect(builder: () -> E) {
        val newEffect = builder()
        launch { _effect.send(newEffect) }
    }

    protected open fun handleException(exception: Throwable) {
        exception.printStackTrace()
    }

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        errorHandler: CoroutineExceptionHandler = _errorHandler,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = (viewModelScope + errorHandler).launch(context, start, block)

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

    protected fun <T> async(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> T,
    ): Deferred<T> = viewModelScope.async(context, start, block)

    fun clear() { onCleared() }
}
