package jpyoon.example.visionfolio.feature.dividend

import jpyoon.example.visionfolio.core.common.ViewEffect
import jpyoon.example.visionfolio.core.common.ViewIntent
import jpyoon.example.visionfolio.core.common.ViewState
import jpyoon.example.visionfolio.domain.model.GuruProfile
import jpyoon.example.visionfolio.domain.model.MarketIndex
import jpyoon.example.visionfolio.domain.model.NewsItem

data class DividendState(
    val isLoading: Boolean = false,
    val indices: List<MarketIndex> = emptyList(),
    val news: List<NewsItem> = emptyList(),
    val gurus: List<GuruProfile> = emptyList(),
) : ViewState

sealed interface DividendIntent : ViewIntent {
    object Refresh : DividendIntent
    data class OpenNews(val id: String) : DividendIntent
    data class OpenIndex(val name: String) : DividendIntent
    data class OpenGuru(val guruId: String) : DividendIntent
}

sealed interface DividendEffect : ViewEffect {
    data class NavigateToGuru(val guruId: String) : DividendEffect
}
