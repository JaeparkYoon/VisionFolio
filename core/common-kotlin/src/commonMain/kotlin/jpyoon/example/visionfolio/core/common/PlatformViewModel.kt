package jpyoon.example.visionfolio.core.common

import kotlinx.coroutines.CoroutineScope

expect abstract class PlatformViewModel() {
    val viewModelScope: CoroutineScope
    protected open fun onCleared()
}
