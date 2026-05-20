package jpyoon.example.visionfolio.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseUseCase<in P, R> {
    abstract suspend fun execute(params: P?): R

    suspend operator fun invoke(params: P?): Result<R> = runCatching { execute(params) }
}

abstract class CoroutineUseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) : BaseUseCase<P, R>() {
    override suspend fun execute(params: P?): R = withContext(dispatcher) { run(params) }

    protected abstract suspend fun run(params: P?): R
}

internal expect val ioDispatcher: CoroutineDispatcher

abstract class IOUseCase<in P, R>(
    dispatcher: CoroutineDispatcher = ioDispatcher,
) : CoroutineUseCase<P, R>(dispatcher)
