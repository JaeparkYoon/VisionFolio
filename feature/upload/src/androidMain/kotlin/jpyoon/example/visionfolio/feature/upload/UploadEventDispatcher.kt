package jpyoon.example.visionfolio.feature.upload

import jpyoon.example.visionfolio.core.analytics.event.EventDispatcher
import jpyoon.example.visionfolio.core.analytics.event.EventTracker
import jpyoon.example.visionfolio.core.analytics.event.EventTrackingType
import jpyoon.example.visionfolio.core.analytics.event.Events
import jpyoon.example.visionfolio.feature.upload.event.UploadEvents

class UploadEventDispatcher(
    private val viewModel: UploadViewModel,
    private val eventTracker: EventTracker,
) : EventDispatcher {

    override fun dispatchEvent(events: Events) {
        when (events) {
            UploadEvents.ViewedUpload -> eventTracker.send(EventTrackingType.VIEWED_UPLOAD)
            else -> Unit
        }
    }
}
