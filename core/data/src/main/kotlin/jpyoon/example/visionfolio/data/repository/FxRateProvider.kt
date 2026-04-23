package jpyoon.example.visionfolio.data.repository

interface FxRateProvider {
    suspend fun usdToKrw(): Double
}
