package jpyoon.example.visionfolio.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 모든 UseCase의 공통 베이스.
 */
abstract class BaseUseCase<in P, R> {
    abstract suspend fun execute(params: P?): R

    suspend operator fun invoke(params: P?): Result<R> = runCatching { execute(params) }
}

/**
 * Default Dispatcher에서 실행되는 UseCase.
 */
abstract class CoroutineUseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : BaseUseCase<P, R>() {
    override suspend fun execute(params: P?): R = withContext(dispatcher) { run(params) }

    protected abstract suspend fun run(params: P?): R
}

/**
 * IO Dispatcher에서 실행되는 UseCase.
 */
abstract class IOUseCase<in P, R>(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CoroutineUseCase<P, R>(dispatcher)
