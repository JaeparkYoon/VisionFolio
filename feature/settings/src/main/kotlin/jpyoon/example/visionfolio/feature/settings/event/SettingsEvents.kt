package jpyoon.example.visionfolio.feature.settings.event

import jpyoon.example.visionfolio.core.analytics.event.Events

sealed interface SettingsEvents : Events {
    data object ViewedSettings : SettingsEvents
    data object ClickedImport : SettingsEvents
}
