package jpyoon.example.visionfolio.feature.home

import jpyoon.example.visionfolio.core.common.MVIViewModel
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.core.repository.api.AppPrefsRepository
import jpyoon.example.visionfolio.core.repository.api.FxRateProvider
import jpyoon.example.visionfolio.domain.usecase.ObserveDividendData
import jpyoon.example.visionfolio.domain.usecase.ObserveHomeData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

class HomeViewModel @Inject constructor(
    private val observeHomeData: ObserveHomeData,
    private val observeDividendData: ObserveDividendData,
    private val appPrefsRepo: AppPrefsRepository,
    private val fxRateProvider: FxRateProvider,
) : MVIViewModel<HomeIntent, HomeState, HomeEffect>() {

    override fun createInitialState(): HomeState = HomeState(isLoading = true)

    init {
        observeHomeData().onEach { data ->
            setState {
                copy(
                    isLoading = false,
                    quote = data.quote,
                    summary = data.summary,
                    holdings = data.holdings,
                    displayCurrency = data.displayCurrency,
                )
            }
        }.launchIn(viewModelScope)

        observeDividendData().onEach { summary ->
            setState { copy(dividendSummary = summary) }
        }.launchIn(viewModelScope)

        appPrefsRepo.observePrefs().onEach { prefs ->
            setState { copy(hideAmounts = prefs.hideAmounts) }
        }.launchIn(viewModelScope)

        viewModelScope.launch {
            val rate = fxRateProvider.usdToKrw()
            setState { copy(usdKrw = rate) }
        }
    }

    override suspend fun processIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.ToggleHideAmounts -> appPrefsRepo.setHideAmounts(!currentState.hideAmounts)
            HomeIntent.ToggleDisplayCurrency -> {
                val next = if (currentState.displayCurrency == Currency.KRW) Currency.USD else Currency.KRW
                appPrefsRepo.setDisplayCurrency(next)
            }
            is HomeIntent.FilterCategory -> setState { copy(visibleCategory = intent.category) }
            HomeIntent.OpenUploadSheet -> setEffect { HomeEffect.NavigateToUpload }
            HomeIntent.OpenAllHoldings -> setEffect { HomeEffect.NavigateToHoldings }
            is HomeIntent.SelectTab -> setState { copy(selectedTab = intent.tab) }
            HomeIntent.OpenTrend -> setEffect { HomeEffect.NavigateToTrend }
        }
    }
}
