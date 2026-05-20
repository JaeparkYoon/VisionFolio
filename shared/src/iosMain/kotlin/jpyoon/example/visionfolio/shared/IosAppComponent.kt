package jpyoon.example.visionfolio.shared

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
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
import jpyoon.example.visionfolio.core.repository.api.ChatRepository
import jpyoon.example.visionfolio.core.repository.api.CreditRepository
import jpyoon.example.visionfolio.core.repository.api.DividendOverrideRepository
import jpyoon.example.visionfolio.core.repository.api.DividendRepository
import jpyoon.example.visionfolio.core.repository.api.FxRateProvider
import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import jpyoon.example.visionfolio.core.repository.api.MarketRepository
import jpyoon.example.visionfolio.core.repository.api.QuoteRepository
import jpyoon.example.visionfolio.core.repository.api.ReturnEntryRepository
import jpyoon.example.visionfolio.core.repository.api.SeriesRepository
import jpyoon.example.visionfolio.data.announcement.AnnouncementRepositoryImpl
import jpyoon.example.visionfolio.data.appupdate.AppUpdateConfigRepositoryImpl
import jpyoon.example.visionfolio.data.chat.ChatRepositoryImpl
import jpyoon.example.visionfolio.data.credit.CreditRepositoryImpl
import jpyoon.example.visionfolio.data.market.DividendOverrideRepositoryImpl
import jpyoon.example.visionfolio.data.market.DividendRepositoryImpl
import jpyoon.example.visionfolio.data.market.FxRateProviderImpl
import jpyoon.example.visionfolio.data.market.MarketRepositoryImpl
import jpyoon.example.visionfolio.data.market.QuoteRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.HoldingRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.ReturnEntryRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.db.VfDatabase
import jpyoon.example.visionfolio.data.portfolio.db.createVfDatabase
import jpyoon.example.visionfolio.data.portfolio.db.holding.HoldingDao
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendCacheDao
import jpyoon.example.visionfolio.data.portfolio.db.dividend.DividendOverrideDao
import jpyoon.example.visionfolio.data.portfolio.db.importsource.ImportSourceDao
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotDao
import jpyoon.example.visionfolio.data.portfolio.db.chat.ChatDao
import jpyoon.example.visionfolio.data.portfolio.db.returns.ReturnEntryDao
import jpyoon.example.visionfolio.domain.usecase.AddHolding
import jpyoon.example.visionfolio.domain.usecase.CommitParsedHoldings
import jpyoon.example.visionfolio.domain.usecase.ExportHoldingsCsv
import jpyoon.example.visionfolio.domain.usecase.ExportPortfolioCsvUseCase
import jpyoon.example.visionfolio.domain.usecase.ImportPortfolioCsvUseCase
import jpyoon.example.visionfolio.domain.usecase.ObserveDividendData
import jpyoon.example.visionfolio.domain.usecase.ObserveHomeData
import jpyoon.example.visionfolio.domain.usecase.ObserveTrendData
import jpyoon.example.visionfolio.domain.usecase.chat.CreateChatSessionUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.DeleteChatSessionUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.ObserveChatSessionsUseCase
import jpyoon.example.visionfolio.domain.usecase.chat.SendChatMessageUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.DeleteReturnEntryUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.GetYtdSummaryUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.ObserveReturnsUseCase
import jpyoon.example.visionfolio.domain.usecase.returns.UpsertReturnEntryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

/**
 * iOS DI component mirroring Android's AppComponent.
 * Uses the same commonMain repository implementations with mock data.
 */
@AppSingleton
@Component
abstract class IosAppComponent {

    // -- Public entry points (accessed from Swift) --
    abstract val holdingRepository: HoldingRepository
    abstract val marketRepository: MarketRepository
    abstract val appPrefsRepository: AppPrefsRepository
    abstract val addHolding: AddHolding
    abstract val dividendOverrideRepository: DividendOverrideRepository
    abstract val exportHoldingsCsv: ExportHoldingsCsv
    abstract val commitParsedHoldings: CommitParsedHoldings
    abstract val observeHomeData: ObserveHomeData

    // -- Coroutines --
    @Provides
    @AppSingleton
    @AppCoroutineScope
    fun appScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // -- Network --
    @Provides
    @AppSingleton
    fun json(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @AppSingleton
    fun httpClient(json: Json): HttpClient = HttpClient(Darwin) {
        install(ContentNegotiation) { json(json) }
        install(HttpTimeout) {
            connectTimeoutMillis = 10_000
            requestTimeoutMillis = 15_000
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            retryOnException(maxRetries = 3, retryOnTimeout = true)
            exponentialDelay()
        }
        install(Logging) { level = LogLevel.INFO }
        defaultRequest {
            header("User-Agent", "VisionFolio/1.0 iOS")
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

    // -- Local DB + DAOs --
    @Provides @AppSingleton fun vfDatabase(): VfDatabase = createVfDatabase()
    @Provides fun holdingDao(db: VfDatabase): HoldingDao = db.holdingDao()
    @Provides fun snapshotDao(db: VfDatabase): SnapshotDao = db.snapshotDao()
    @Provides fun importSourceDao(db: VfDatabase): ImportSourceDao = db.importSourceDao()
    @Provides fun dividendCacheDao(db: VfDatabase): DividendCacheDao = db.dividendCacheDao()
    @Provides fun dividendOverrideDao(db: VfDatabase): DividendOverrideDao = db.dividendOverrideDao()
    @Provides fun chatDao(db: VfDatabase): ChatDao = db.chatDao()
    @Provides fun returnEntryDao(db: VfDatabase): ReturnEntryDao = db.returnEntryDao()

    // -- API Keys (empty = fallback behavior) --
    @Provides @EcosApiKey fun ecosApiKey(): String = ""
    @Provides @NewsApiKey fun newsApiKey(): String = ""
    @Provides @NaverClientId fun naverClientId(): String = ""
    @Provides @NaverClientSecret fun naverClientSecret(): String = ""
    @Provides @TwelveDataApiKey fun twelveDataApiKey(): String = ""
    @Provides @FinnhubApiKey fun finnhubApiKey(): String = ""
    @Provides @KrDividendApiKey fun krDividendApiKey(): String = ""

    // -- Repository bindings (impl -> interface) --
    @Provides @AppSingleton fun holdingRepository(impl: HoldingRepositoryImpl): HoldingRepository = impl
    @Provides @AppSingleton fun marketRepository(impl: MarketRepositoryImpl): MarketRepository = impl
    @Provides @AppSingleton fun quoteRepository(impl: QuoteRepositoryImpl): QuoteRepository = impl
    @Provides @AppSingleton fun fxRateProvider(impl: FxRateProviderImpl): FxRateProvider = impl
    @Provides @AppSingleton fun dividendRepository(impl: DividendRepositoryImpl): DividendRepository = impl
    @Provides @AppSingleton fun dividendOverrideRepository(impl: DividendOverrideRepositoryImpl): DividendOverrideRepository = impl
    @Provides @AppSingleton fun creditRepository(impl: CreditRepositoryImpl): CreditRepository = impl
    @Provides @AppSingleton fun announcementRepository(impl: AnnouncementRepositoryImpl): AnnouncementRepository = impl
    @Provides @AppSingleton fun appUpdateConfigRepository(impl: AppUpdateConfigRepositoryImpl): AppUpdateConfigRepository = impl
    @Provides @AppSingleton fun chatRepository(impl: ChatRepositoryImpl): ChatRepository = impl
    @Provides @AppSingleton fun returnEntryRepository(impl: ReturnEntryRepositoryImpl): ReturnEntryRepository = impl
    @Provides @AppSingleton fun appPrefsRepository(impl: IosAppPrefsRepository): AppPrefsRepository = impl
    @Provides @AppSingleton fun seriesRepository(impl: IosSeriesRepository): SeriesRepository = impl

    // -- UseCases --
    @Provides fun addHolding(r: HoldingRepository): AddHolding = AddHolding(r)
    @Provides fun commitParsedHoldings(r: HoldingRepository): CommitParsedHoldings = CommitParsedHoldings(r)
    @Provides fun exportHoldingsCsv(r: HoldingRepository): ExportHoldingsCsv = ExportHoldingsCsv(r)
    @Provides fun exportPortfolioCsvUseCase(h: HoldingRepository, r: ReturnEntryRepository): ExportPortfolioCsvUseCase = ExportPortfolioCsvUseCase(h, r)
    @Provides fun importPortfolioCsvUseCase(h: HoldingRepository, r: ReturnEntryRepository): ImportPortfolioCsvUseCase = ImportPortfolioCsvUseCase(h, r)
    @Provides fun observeHomeData(h: HoldingRepository, q: QuoteRepository, fx: FxRateProvider, p: AppPrefsRepository): ObserveHomeData = ObserveHomeData(h, q, fx, p)
    @Provides fun observeTrendData(s: SeriesRepository): ObserveTrendData = ObserveTrendData(s)
    @Provides fun observeDividendData(h: HoldingRepository, d: DividendRepository, fx: FxRateProvider): ObserveDividendData = ObserveDividendData(h, d, fx)
    @Provides fun createChatSessionUseCase(c: ChatRepository): CreateChatSessionUseCase = CreateChatSessionUseCase(c)
    @Provides fun deleteChatSessionUseCase(c: ChatRepository): DeleteChatSessionUseCase = DeleteChatSessionUseCase(c)
    @Provides fun observeChatSessionsUseCase(c: ChatRepository): ObserveChatSessionsUseCase = ObserveChatSessionsUseCase(c)
    @Provides fun sendChatMessageUseCase(c: ChatRepository, cr: CreditRepository): SendChatMessageUseCase = SendChatMessageUseCase(c, cr)
    @Provides fun observeReturnsUseCase(r: ReturnEntryRepository): ObserveReturnsUseCase = ObserveReturnsUseCase(r)
    @Provides fun upsertReturnEntryUseCase(r: ReturnEntryRepository): UpsertReturnEntryUseCase = UpsertReturnEntryUseCase(r)
    @Provides fun deleteReturnEntryUseCase(r: ReturnEntryRepository): DeleteReturnEntryUseCase = DeleteReturnEntryUseCase(r)
    @Provides fun getYtdSummaryUseCase(r: ReturnEntryRepository): GetYtdSummaryUseCase = GetYtdSummaryUseCase(r)
}
