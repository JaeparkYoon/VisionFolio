package jpyoon.example.visionfolio.app.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import jpyoon.example.visionfolio.core.common.di.AppCoroutineScope
import jpyoon.example.visionfolio.core.common.di.AppSingleton
import jpyoon.example.visionfolio.core.common.di.EcosApiKey
import jpyoon.example.visionfolio.core.common.di.FinnhubApiKey
import jpyoon.example.visionfolio.core.common.di.KrDividendApiKey
import jpyoon.example.visionfolio.core.common.di.NaverClientId
import jpyoon.example.visionfolio.core.common.di.NaverClientSecret
import jpyoon.example.visionfolio.core.common.di.NewsApiKey
import jpyoon.example.visionfolio.core.common.di.TwelveDataApiKey
import jpyoon.example.visionfolio.core.network.EcosService
import jpyoon.example.visionfolio.core.network.FinnhubDividendService
import jpyoon.example.visionfolio.core.network.KrDividendService
import jpyoon.example.visionfolio.core.network.NaverIndicesService
import jpyoon.example.visionfolio.core.network.NaverNewsService
import jpyoon.example.visionfolio.core.network.NewsService
import jpyoon.example.visionfolio.core.network.TwelveDataService
import jpyoon.example.visionfolio.core.network.YahooFinanceService
import jpyoon.example.visionfolio.core.repository.api.AnnouncementRepository
import jpyoon.example.visionfolio.core.repository.api.AppPrefsRepository
import jpyoon.example.visionfolio.core.repository.api.AppUpdateConfigRepository
import jpyoon.example.visionfolio.core.repository.api.BillingRepository
import jpyoon.example.visionfolio.core.repository.api.ChatRepository
import jpyoon.example.visionfolio.core.repository.api.CreditRepository
import jpyoon.example.visionfolio.core.repository.api.DividendOverrideRepository
import jpyoon.example.visionfolio.core.repository.api.DividendRepository
import jpyoon.example.visionfolio.core.repository.api.FxRateProvider
import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import jpyoon.example.visionfolio.core.repository.api.ImportSourceRepository
import jpyoon.example.visionfolio.core.repository.api.MarketRepository
import jpyoon.example.visionfolio.core.repository.api.QuoteRepository
import jpyoon.example.visionfolio.core.repository.api.ReturnEntryRepository
import jpyoon.example.visionfolio.core.repository.api.ScreenshotParser
import jpyoon.example.visionfolio.core.repository.api.SeriesRepository
import jpyoon.example.visionfolio.data.announcement.AnnouncementRepositoryImpl
import jpyoon.example.visionfolio.data.appupdate.AppUpdateConfigRepositoryImpl
import jpyoon.example.visionfolio.data.billing.BillingRepositoryImpl
import jpyoon.example.visionfolio.data.chat.ChatRepositoryImpl
import jpyoon.example.visionfolio.data.credit.CreditRepositoryImpl
import jpyoon.example.visionfolio.data.market.DividendOverrideRepositoryImpl
import jpyoon.example.visionfolio.data.market.DividendRepositoryImpl
import jpyoon.example.visionfolio.data.market.FxRateProviderImpl
import jpyoon.example.visionfolio.data.market.MarketRepositoryImpl
import jpyoon.example.visionfolio.data.market.QuoteRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.HoldingRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.ImportSourceRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.ReturnEntryRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.SeriesRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.SnapshotRecorder
import jpyoon.example.visionfolio.data.portfolio.db.VfDatabase
import jpyoon.example.visionfolio.data.portfolio.db.createVfDatabase
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendCacheDao
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendOverrideDao
import jpyoon.example.visionfolio.data.portfolio.db.holding.HoldingDao
import jpyoon.example.visionfolio.data.portfolio.db.importsource.ImportSourceDao
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotDao
import jpyoon.example.visionfolio.data.prefs.AppPrefsRepositoryImpl
import jpyoon.example.visionfolio.data.prefs.appPrefsDataStore
import jpyoon.example.visionfolio.data.ai.ScreenshotParserImpl
import jpyoon.example.visionfolio.domain.usecase.AddHoldingUseCase
import jpyoon.example.visionfolio.domain.usecase.CommitParsedHoldingsUseCase
import jpyoon.example.visionfolio.domain.usecase.ExportHoldingsCsvUseCase
import jpyoon.example.visionfolio.domain.usecase.ExportPortfolioCsvUseCase
import jpyoon.example.visionfolio.domain.usecase.ImportPortfolioCsvUseCase
import jpyoon.example.visionfolio.domain.usecase.ObserveDividendDataUseCase
import jpyoon.example.visionfolio.domain.usecase.ObserveHomeDataUseCase
import jpyoon.example.visionfolio.domain.usecase.ObserveTrendDataUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.CreateChatSessionUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.DeleteChatSessionUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.ObserveChatSessionsUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.SendChatMessageUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.DeleteReturnEntryUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.GetYtdSummaryUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.ObserveReturnsUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.UpsertReturnEntryUseCase
import jpyoon.example.visionfolio.feature.addholding.AddHoldingViewModel
import jpyoon.example.visionfolio.feature.addholding.editlist.EditHoldingsViewModel
import jpyoon.example.visionfolio.feature.chat.ChatViewModel
import jpyoon.example.visionfolio.feature.dividend.DividendViewModel
import jpyoon.example.visionfolio.feature.home.HomeViewModel
import jpyoon.example.visionfolio.feature.returns.ReturnsViewModel
import jpyoon.example.visionfolio.feature.settings.SettingsViewModel
import jpyoon.example.visionfolio.feature.trend.TrendViewModel
import jpyoon.example.visionfolio.feature.tweaks.TweaksViewModel
import jpyoon.example.visionfolio.feature.upload.UploadViewModel
import jpyoon.example.visionfolio.notification.NotificationScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import java.util.concurrent.TimeUnit

@AppSingleton
@Component
abstract class AppComponent(
    @get:Provides val application: Application,
) {
    // ── Application 측 inject 대상 노출 ─────────────────
    abstract val appPrefsRepository: AppPrefsRepository
    abstract val notificationScheduler: NotificationScheduler
    abstract val snapshotRecorder: SnapshotRecorder
    abstract val creditRepository: CreditRepository

    @get:AppCoroutineScope
    abstract val appScope: CoroutineScope

    // ── ViewModel factories ─────────────────────────────
    abstract val homeViewModel: () -> HomeViewModel
    abstract val trendViewModel: () -> TrendViewModel
    abstract val dividendViewModel: () -> DividendViewModel
    abstract val settingsViewModel: () -> SettingsViewModel
    abstract val chatViewModel: () -> ChatViewModel
    abstract val addHoldingViewModel: () -> AddHoldingViewModel
    abstract val editHoldingsViewModel: () -> EditHoldingsViewModel
    abstract val returnsViewModel: () -> ReturnsViewModel
    abstract val tweaksViewModel: () -> TweaksViewModel

    abstract val uploadViewModelFactory: (androidx.lifecycle.SavedStateHandle) -> UploadViewModel

    abstract val screenshotParser: ScreenshotParser
    abstract val screenshotParseSession: jpyoon.example.visionfolio.data.ai.ScreenshotParseSession
    abstract val screenshotParseResultStore: jpyoon.example.visionfolio.data.ai.ScreenshotParseResultStore

    abstract val billingRepository: BillingRepository
    abstract val observeHomeDataUseCase: ObserveHomeDataUseCase

    // ── Context / API keys (all empty for demo) ─────────
    @Provides fun context(application: Application): Context = application

    @Provides @NewsApiKey fun newsApiKey(): String = ""
    @Provides @KrDividendApiKey fun krDividendApiKey(): String = ""
    @Provides @FinnhubApiKey fun finnhubApiKey(): String = ""
    @Provides @NaverClientId fun naverClientId(): String = ""
    @Provides @NaverClientSecret fun naverClientSecret(): String = ""
    @Provides @TwelveDataApiKey fun twelveDataApiKey(): String = ""
    @Provides @EcosApiKey fun ecosApiKey(): String = ""

    // ── Coroutines ──────────────────────────────────────
    @Provides
    @AppSingleton
    @AppCoroutineScope
    fun provideAppScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // ── DataStore ───────────────────────────────────────
    @Provides
    @AppSingleton
    fun dataStore(context: Context): DataStore<Preferences> = context.appPrefsDataStore

    // ── Network: Json + HttpClient + Services ───────────
    @Provides
    @AppSingleton
    fun json(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @AppSingleton
    fun httpClient(json: Json): HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) { json(json) }
        install(HttpTimeout) {
            connectTimeoutMillis = TimeUnit.SECONDS.toMillis(10)
            requestTimeoutMillis = TimeUnit.SECONDS.toMillis(15)
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            retryOnException(maxRetries = 3, retryOnTimeout = true)
            exponentialDelay()
        }
        install(Logging) { level = LogLevel.NONE }
        defaultRequest {
            header(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 14; VisionFolio) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/124.0.0.0 Mobile Safari/537.36",
            )
            header("Accept", "application/json,text/plain,*/*")
        }
    }

    @Provides @AppSingleton fun ecosService(c: HttpClient): EcosService = EcosService(c)
    @Provides @AppSingleton fun finnhubService(c: HttpClient): FinnhubDividendService = FinnhubDividendService(c)
    @Provides @AppSingleton fun krDividendService(c: HttpClient): KrDividendService = KrDividendService(c)
    @Provides @AppSingleton fun naverIndicesService(c: HttpClient): NaverIndicesService = NaverIndicesService(c)
    @Provides @AppSingleton fun naverNewsService(c: HttpClient): NaverNewsService = NaverNewsService(c)
    @Provides @AppSingleton fun newsService(c: HttpClient): NewsService = NewsService(c)
    @Provides @AppSingleton fun twelveDataService(c: HttpClient): TwelveDataService = TwelveDataService(c)
    @Provides @AppSingleton fun yahooFinanceService(c: HttpClient): YahooFinanceService = YahooFinanceService(c)

    // ── Local DB + DAOs ─────────────────────────────────
    @Provides @AppSingleton fun vfDatabase(context: Context): VfDatabase = createVfDatabase(context)
    @Provides fun holdingDao(db: VfDatabase): HoldingDao = db.holdingDao()
    @Provides fun snapshotDao(db: VfDatabase): SnapshotDao = db.snapshotDao()
    @Provides fun importSourceDao(db: VfDatabase): ImportSourceDao = db.importSourceDao()
    @Provides fun dividendCacheDao(db: VfDatabase): DividendCacheDao = db.dividendCacheDao()
    @Provides fun dividendOverrideDao(db: VfDatabase): DividendOverrideDao = db.dividendOverrideDao()

    // ── Repository bindings (impl → interface) ──────────
    @Provides @AppSingleton fun holdingRepository(i: HoldingRepositoryImpl): HoldingRepository = i
    @Provides @AppSingleton fun marketRepository(i: MarketRepositoryImpl): MarketRepository = i
    @Provides @AppSingleton fun quoteRepository(i: QuoteRepositoryImpl): QuoteRepository = i
    @Provides @AppSingleton fun fxRateProvider(i: FxRateProviderImpl): FxRateProvider = i
    @Provides @AppSingleton fun seriesRepository(i: SeriesRepositoryImpl): SeriesRepository = i
    @Provides @AppSingleton fun screenshotParser(i: ScreenshotParserImpl): ScreenshotParser = i
    @Provides @AppSingleton fun appPrefsRepository(i: AppPrefsRepositoryImpl): AppPrefsRepository = i
    @Provides @AppSingleton fun importSourceRepository(i: ImportSourceRepositoryImpl): ImportSourceRepository = i
    @Provides @AppSingleton fun dividendRepository(i: DividendRepositoryImpl): DividendRepository = i
    @Provides @AppSingleton fun dividendOverrideRepository(i: DividendOverrideRepositoryImpl): DividendOverrideRepository = i
    @Provides @AppSingleton fun creditRepository(i: CreditRepositoryImpl): CreditRepository = i
    @Provides @AppSingleton fun billingRepository(i: BillingRepositoryImpl): BillingRepository = i
    @Provides @AppSingleton fun appUpdateConfigRepository(i: AppUpdateConfigRepositoryImpl): AppUpdateConfigRepository = i
    @Provides @AppSingleton fun announcementRepository(i: AnnouncementRepositoryImpl): AnnouncementRepository = i
    @Provides @AppSingleton fun chatRepository(i: ChatRepositoryImpl): ChatRepository = i
    @Provides @AppSingleton fun returnEntryRepository(i: ReturnEntryRepositoryImpl): ReturnEntryRepository = i

    // ── UseCases ────────────────────────────────────────
    @Provides fun addHoldingUseCase(r: HoldingRepository): AddHoldingUseCase = AddHoldingUseCase(r)
    @Provides fun commitParsedHoldingsUseCase(r: HoldingRepository): CommitParsedHoldingsUseCase = CommitParsedHoldingsUseCase(r)
    @Provides fun exportHoldingsCsvUseCase(r: HoldingRepository): ExportHoldingsCsvUseCase = ExportHoldingsCsvUseCase(r)
    @Provides fun exportPortfolioCsvUseCase(h: HoldingRepository, r: ReturnEntryRepository): ExportPortfolioCsvUseCase = ExportPortfolioCsvUseCase(h, r)
    @Provides fun importPortfolioCsvUseCase(h: HoldingRepository, r: ReturnEntryRepository): ImportPortfolioCsvUseCase = ImportPortfolioCsvUseCase(h, r)
    @Provides fun observeDividendDataUseCase(h: HoldingRepository, d: DividendRepository, fx: FxRateProvider, o: DividendOverrideRepository): ObserveDividendDataUseCase = ObserveDividendDataUseCase(h, d, fx, o)
    @Provides fun observeHomeDataUseCase(h: HoldingRepository, q: QuoteRepository, fx: FxRateProvider, p: AppPrefsRepository, s: SeriesRepository): ObserveHomeDataUseCase = ObserveHomeDataUseCase(h, q, fx, p, s)
    @Provides fun observeTrendDataUseCase(s: SeriesRepository): ObserveTrendDataUseCase = ObserveTrendDataUseCase(s)
    @Provides fun createChatSessionUseCase(c: ChatRepository): CreateChatSessionUseCase = CreateChatSessionUseCase(c)
    @Provides fun deleteChatSessionUseCase(c: ChatRepository): DeleteChatSessionUseCase = DeleteChatSessionUseCase(c)
    @Provides fun observeChatSessionsUseCase(c: ChatRepository): ObserveChatSessionsUseCase = ObserveChatSessionsUseCase(c)
    @Provides fun sendChatMessageUseCase(c: ChatRepository, cr: CreditRepository): SendChatMessageUseCase = SendChatMessageUseCase(c, cr)
    @Provides fun observeReturnsUseCase(r: ReturnEntryRepository): ObserveReturnsUseCase = ObserveReturnsUseCase(r)
    @Provides fun upsertReturnEntryUseCase(r: ReturnEntryRepository): UpsertReturnEntryUseCase = UpsertReturnEntryUseCase(r)
    @Provides fun deleteReturnEntryUseCase(r: ReturnEntryRepository): DeleteReturnEntryUseCase = DeleteReturnEntryUseCase(r)
    @Provides fun getYtdSummaryUseCase(r: ReturnEntryRepository): GetYtdSummaryUseCase = GetYtdSummaryUseCase(r)
}
