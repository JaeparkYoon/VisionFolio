package jpyoon.example.visionfolio.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

actual abstract class PlatformViewModel actual constructor() {
    actual val viewModelScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main)

    protected actual open fun onCleared() {
        viewModelScope.cancel()
    }
}
