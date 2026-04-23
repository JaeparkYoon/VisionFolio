package jpyoon.example.visionfolio.core.analytics.event

import org.json.JSONObject

/**
 * 이벤트 추적 인터페이스.
 */
interface EventTracker {
    fun send(type: EventTrackingType, params: JSONObject = JSONObject())
}

class NoOpEventTracker : EventTracker {
    override fun send(type: EventTrackingType, params: JSONObject) = Unit
}
