package jpyoon.example.visionfolio.domain.model

val AssetCategory.displayName: String
    get() = when (this) {
        AssetCategory.DOMESTIC_STOCK -> "국내주식"
        AssetCategory.OVERSEAS_STOCK -> "해외주식"
        AssetCategory.ETF -> "ETF"
        AssetCategory.CRYPTO -> "암호화폐"
        AssetCategory.BOND -> "채권"
        AssetCategory.CASH -> "예적금"
        AssetCategory.PENSION -> "연금/IRP"
        AssetCategory.SAVINGS -> "적금"
        AssetCategory.OTHER -> "기타"
    }
