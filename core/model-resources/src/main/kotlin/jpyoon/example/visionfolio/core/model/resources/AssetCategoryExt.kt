package jpyoon.example.visionfolio.core.model.resources

import androidx.annotation.StringRes
import jpyoon.example.visionfolio.core.model.resources.R
import jpyoon.example.visionfolio.domain.model.AssetCategory

val AssetCategory.displayNameRes: Int
    @StringRes get() = when (this) {
        AssetCategory.DOMESTIC_STOCK -> R.string.category_domestic_stock
        AssetCategory.OVERSEAS_STOCK -> R.string.category_overseas_stock
        AssetCategory.ETF -> R.string.category_etf
        AssetCategory.CRYPTO -> R.string.category_crypto
        AssetCategory.BOND -> R.string.category_bond
        AssetCategory.CASH -> R.string.category_cash
        AssetCategory.PENSION -> R.string.category_pension
        AssetCategory.SAVINGS -> R.string.category_savings
        AssetCategory.OTHER -> R.string.category_other
    }
