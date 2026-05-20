package jpyoon.example.visionfolio.feature.chat

import jpyoon.example.visionfolio.core.analytics.event.EventDispatcher
import jpyoon.example.visionfolio.core.analytics.event.EventTracker
import jpyoon.example.visionfolio.core.analytics.event.Events

class ChatEventDispatcher(
    private val viewModel: ChatViewModel,
    private val eventTracker: EventTracker,
) : EventDispatcher {

    override fun dispatchEvent(events: Events) {
        when (events) {
            else -> Unit
        }
    }
}
