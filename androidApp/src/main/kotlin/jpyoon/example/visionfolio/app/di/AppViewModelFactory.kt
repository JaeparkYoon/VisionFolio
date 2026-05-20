package jpyoon.example.visionfolio.app.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import jpyoon.example.visionfolio.feature.addholding.AddHoldingViewModel
import jpyoon.example.visionfolio.feature.addholding.editlist.EditHoldingsViewModel
import jpyoon.example.visionfolio.feature.chat.ChatViewModel
import jpyoon.example.visionfolio.feature.dividend.DividendViewModel
import jpyoon.example.visionfolio.feature.home.HomeViewModel
import jpyoon.example.visionfolio.feature.returns.ReturnsViewModel
import jpyoon.example.visionfolio.feature.settings.SettingsViewModel
import jpyoon.example.visionfolio.feature.trend.TrendViewModel
import jpyoon.example.visionfolio.feature.tweaks.TweaksViewModel
import jpyoon.example.visionfolio.feature.upload.UploadViewModel

class AppViewModelFactory(
    private val component: AppComponent,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        @Suppress("UNCHECKED_CAST")
        return when (modelClass) {
            HomeViewModel::class.java -> component.homeViewModel()
            TrendViewModel::class.java -> component.trendViewModel()
            DividendViewModel::class.java -> component.dividendViewModel()
            SettingsViewModel::class.java -> component.settingsViewModel()
            ChatViewModel::class.java -> component.chatViewModel()
            AddHoldingViewModel::class.java -> component.addHoldingViewModel()
            EditHoldingsViewModel::class.java -> component.editHoldingsViewModel()
            ReturnsViewModel::class.java -> component.returnsViewModel()
            TweaksViewModel::class.java -> component.tweaksViewModel()
            UploadViewModel::class.java -> {
                val savedStateHandle: SavedStateHandle = extras.createSavedStateHandle()
                component.uploadViewModelFactory(savedStateHandle)
            }
            else -> error("Unknown ViewModel class: $modelClass")
        } as T
    }
}
