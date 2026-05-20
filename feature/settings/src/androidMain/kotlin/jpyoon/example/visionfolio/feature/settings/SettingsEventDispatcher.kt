package jpyoon.example.visionfolio.feature.settings

import jpyoon.example.visionfolio.core.analytics.event.EventDispatcher
import jpyoon.example.visionfolio.core.analytics.event.EventTracker
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingType
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.feature.settings.event.SettingsEvents

class SettingsEventDispatcher(
    private val viewModel: SettingsViewModel,
    private val eventTracker: EventTracker,
) : EventDispatcher {

    override fun dispatchEvent(events: Events) {
        when (events) {
            SettingsEvents.ViewedSettings -> eventTracker.send(EventTrackingType.VIEWED_SETTINGS)
            SettingsEvents.ClickedImport -> eventTracker.send(EventTrackingType.CLICKED_SETTINGS_IMPORT)
            else -> Unit
        }
    }
}
