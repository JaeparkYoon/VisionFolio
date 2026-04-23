package jpyoon.example.visionfolio.data.market

import jpyoon.example.visionfolio.data.repository.FxRateProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FxRateProviderImpl @Inject constructor() : FxRateProvider {

    override suspend fun usdToKrw(): Double = MOCK_USD_KRW

    private companion object {
        const val MOCK_USD_KRW = 1_388.0
    }
}
