package jpyoon.example.visionfolio.domain.model

data class GuruProfile(
    val id: String,
    val name: String,
    val nameEn: String,
    val title: String,
    val philosophy: String,
    val imageUrl: String,
    val holdings: List<GuruHolding>,
)

data class GuruHolding(
    val name: String,
    val code: String,
    val category: AssetCategory,
    val weight: Double,
    val valueUsd: Long,
    val shares: Long,
)
