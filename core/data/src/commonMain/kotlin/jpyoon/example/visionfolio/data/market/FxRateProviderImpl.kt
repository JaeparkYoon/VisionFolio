package jpyoon.example.visionfolio.data.market

import jpyoon.example.visionfolio.core.repository.api.FxRateProvider
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

@AppSingleton
class FxRateProviderImpl @Inject constructor() : FxRateProvider {

    override suspend fun usdToKrw(): Double = MOCK_USD_KRW

    private companion object {
        const val MOCK_USD_KRW = 1_388.0
    }
}
