package jpyoon.example.visionfolio.core.common.di

import me.tatarka.inject.annotations.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NewsApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AppCoroutineScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KrDividendApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FinnhubApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NaverClientId

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NaverClientSecret

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TwelveDataApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class EcosApiKey
