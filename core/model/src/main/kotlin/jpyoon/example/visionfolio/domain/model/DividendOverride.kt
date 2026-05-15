package jpyoon.example.visionfolio.domain.model

data class DividendOverride(
    val holdingId: String,
    val annualDividendKrw: Long,
    val updatedAt: Long = System.currentTimeMillis(),
)
