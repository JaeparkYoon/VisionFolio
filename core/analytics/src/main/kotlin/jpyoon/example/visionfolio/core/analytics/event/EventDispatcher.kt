package jpyoon.example.visionfolio.core.analytics.event

/**
 * UI 이벤트를 Intent + 분석 추적으로 라우팅하는 디스패처.
 */
interface EventDispatcher {
    fun dispatchEvent(events: Events)
}
