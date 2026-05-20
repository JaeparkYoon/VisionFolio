package jpyoon.example.visionfolio.core.common

inline fun <T> Result<T>.onComplete(
    crossinline doOnSuccess: (T) -> Unit = {},
    crossinline doOnError: (Throwable) -> Unit = {},
    crossinline doOnComplete: () -> Unit = {},
) {
    onSuccess { doOnSuccess(it) }
    onFailure { doOnError(it) }
    doOnComplete()
}
