package jpyoon.example.visionfolio.feature.trend

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpyoon.example.visionfolio.core.android.MVIViewModel
import jpyoon.example.visionfolio.domain.model.Period
import jpyoon.example.visionfolio.data.repository.AppPrefsRepository
import jpyoon.example.visionfolio.data.repository.FxRateProvider
import jpyoon.example.visionfolio.data.repository.SeriesRepository
import jpyoon.example.visionfolio.domain.usecase.ObserveTrendData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrendViewModel @Inject constructor(
    private val observeTrendData: ObserveTrendData,
    private val appPrefsRepo: AppPrefsRepository,
    private val fxRateProvider: FxRateProvider,
    private val seriesRepo: SeriesRepository,
) : MVIViewModel<TrendIntent, TrendState, TrendEffect>() {

    override fun createInitialState(): TrendState = TrendState(isLoading = true)

    private val periodFlow = MutableStateFlow(Period.M1)
    private val customRangeFlow = MutableStateFlow<ClosedRange<Long>?>(null)

    init {
        observeTrendData(periodFlow, customRangeFlow).onEach { data ->
            setState {
                copy(
                    isLoading = false,
                    series = data.series,
                    stats = data.stats,
                    contributions = data.contributions,
                    aiSummary = data.aiSummary,
                )
            }
        }.launchIn(viewModelScope)

        appPrefsRepo.observePrefs()
            .onEach { prefs -> setState { copy(displayCurrency = prefs.displayCurrency) } }
            .launchIn(viewModelScope)

        seriesRepo.observeOldestTimestamp()
            .onEach { oldest ->
                val available = availablePeriods(oldest)
                setState {
                    // 현재 선택된 period 가 더 이상 사용 불가하면 가장 큰 사용 가능 period 로 폴백.
                    val fallback = if (period in available) period else available.firstOrNull { it != Period.CUSTOM } ?: Period.D1
                    if (fallback != period) {
                        periodFlow.value = fallback
                        customRangeFlow.value = null
                        copy(availablePeriods = available, period = fallback, customRange = null)
                    } else {
                        copy(availablePeriods = available)
                    }
                }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val rate = fxRateProvider.usdToKrw()
            setState { copy(usdKrw = rate) }
        }
    }

    private fun availablePeriods(oldestTimestamp: Long?): List<Period> {
        val spanDays = oldestTimestamp
            ?.let { (System.currentTimeMillis() - it) / DAY_MS }
            ?: 0L
        return buildList {
            add(Period.D1)
            if (spanDays >= 7) add(Period.W1)
            if (spanDays >= 30) add(Period.M1)
            if (spanDays >= 90) add(Period.M3)
            if (spanDays >= 180) add(Period.M6)
            if (spanDays >= 365) add(Period.Y1)
            add(Period.ALL)
            add(Period.CUSTOM)
        }
    }

    private companion object {
        const val DAY_MS = 24L * 60L * 60L * 1000L
    }

    override suspend fun processIntent(intent: TrendIntent) {
        when (intent) {
            is TrendIntent.SelectPeriod -> {
                periodFlow.value = intent.period
                customRangeFlow.value = null
                setState { copy(period = intent.period, customRange = null, hoveredIndex = null) }
            }
            is TrendIntent.SelectCustomRange -> {
                val range = intent.from..intent.to
                periodFlow.value = Period.CUSTOM
                customRangeFlow.value = range
                setState { copy(period = Period.CUSTOM, customRange = range, hoveredIndex = null) }
            }
            is TrendIntent.HoverAt -> setState { copy(hoveredIndex = intent.index) }
            TrendIntent.OpenCustomPicker -> setEffect { TrendEffect.ShowCustomRangeSheet }
            TrendIntent.DismissCustomPicker -> setEffect { TrendEffect.DismissCustomRangeSheet }
        }
    }
}
