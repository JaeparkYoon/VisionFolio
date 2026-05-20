package jpyoon.example.visionfolio.core.common

import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope as androidViewModelScope

actual abstract class PlatformViewModel actual constructor() : ViewModel() {
    actual val viewModelScope: CoroutineScope
        get() = androidViewModelScope

    actual override fun onCleared() {
        super.onCleared()
    }
}
