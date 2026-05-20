package jpyoon.example.visionfolio.feature.dividend.event

import jpyoon.example.visionfolio.core.analytics.event.Events

sealed interface DividendEvents : Events {
    data object ViewedDividend : DividendEvents
    data class ClickedOpenNews(val id: String) : DividendEvents
    data class ClickedOpenIndex(val name: String) : DividendEvents
    data class ClickedOpenGuru(val guruId: String) : DividendEvents
}
