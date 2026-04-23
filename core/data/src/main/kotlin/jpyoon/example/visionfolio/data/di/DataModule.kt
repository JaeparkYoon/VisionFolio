package jpyoon.example.visionfolio.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jpyoon.example.visionfolio.data.ai.ScreenshotParserImpl
import jpyoon.example.visionfolio.data.market.DividendRepositoryImpl
import jpyoon.example.visionfolio.data.market.FxRateProviderImpl
import jpyoon.example.visionfolio.data.market.MarketRepositoryImpl
import jpyoon.example.visionfolio.data.market.QuoteRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.HoldingRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.ImportSourceRepositoryImpl
import jpyoon.example.visionfolio.data.portfolio.SeriesRepositoryImpl
import jpyoon.example.visionfolio.data.prefs.AppPrefsRepositoryImpl
import jpyoon.example.visionfolio.data.prefs.appPrefsDataStore
import jpyoon.example.visionfolio.data.repository.AppPrefsRepository
import jpyoon.example.visionfolio.data.repository.DividendRepository
import jpyoon.example.visionfolio.data.repository.FxRateProvider
import jpyoon.example.visionfolio.data.repository.HoldingRepository
import jpyoon.example.visionfolio.data.repository.ImportSourceRepository
import jpyoon.example.visionfolio.data.repository.MarketRepository
import jpyoon.example.visionfolio.data.repository.QuoteRepository
import jpyoon.example.visionfolio.data.repository.ScreenshotParser
import jpyoon.example.visionfolio.data.repository.SeriesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataProviderModule {

    @Provides
    @Singleton
    @AppCoroutineScope
    fun provideAppCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    @Singleton
    fun provideAppPrefsDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = context.appPrefsDataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataBindingModule {

    @Binds
    @Singleton
    abstract fun bindHoldingRepository(impl: HoldingRepositoryImpl): HoldingRepository

    @Binds
    @Singleton
    abstract fun bindMarketRepository(impl: MarketRepositoryImpl): MarketRepository

    @Binds
    @Singleton
    abstract fun bindQuoteRepository(impl: QuoteRepositoryImpl): QuoteRepository

    @Binds
    @Singleton
    abstract fun bindFxRateProvider(impl: FxRateProviderImpl): FxRateProvider

    @Binds
    @Singleton
    abstract fun bindSeriesRepository(impl: SeriesRepositoryImpl): SeriesRepository

    @Binds
    @Singleton
    abstract fun bindScreenshotParser(impl: ScreenshotParserImpl): ScreenshotParser

    @Binds
    @Singleton
    abstract fun bindAppPrefsRepository(impl: AppPrefsRepositoryImpl): AppPrefsRepository

    @Binds
    @Singleton
    abstract fun bindImportSourceRepository(impl: ImportSourceRepositoryImpl): ImportSourceRepository

    @Binds
    @Singleton
    abstract fun bindDividendRepository(impl: DividendRepositoryImpl): DividendRepository
}
