package jpyoon.example.visionfolio.designsystem.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

val LocalViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("LocalViewModelFactory not provided. Wrap setContent with CompositionLocalProvider.")
}

@Composable
inline fun <reified VM : ViewModel> injectedViewModel(): VM = viewModel(
    viewModelStoreOwner = LocalViewModelStoreOwner.current
        ?: error("No ViewModelStoreOwner in current composition"),
    factory = LocalViewModelFactory.current,
)
