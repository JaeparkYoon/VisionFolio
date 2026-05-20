package jpyoon.example.visionfolio.core.repository.api

interface FxRateProvider {
    suspend fun usdToKrw(): Double
}
