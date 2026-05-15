package jpyoon.example.visionfolio.feature.addholding

import jpyoon.example.visionfolio.core.android.ViewIntent
import jpyoon.example.visionfolio.core.android.ViewEffect
import jpyoon.example.visionfolio.core.android.ViewState
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.isBond
import jpyoon.example.visionfolio.domain.model.isCash

data class AddHoldingState(
    val editingId: String? = null,
    val category: AssetCategory = AssetCategory.DOMESTIC_STOCK,
    val currency: Currency = Currency.KRW,
    val name: String = "",
    val code: String = "",
    val quantity: String = "",
    val currentValue: String = "",
    val maturityDate: String = "",
    val excludedFromAllocation: Boolean = false,
    val isSubmitting: Boolean = false,
    val error: String? = null,
) : ViewState {
    val canSubmit: Boolean
        get() = name.isNotBlank() &&
            currentValue.toDoubleOrNull()?.let { it >= 0 } == true
}

sealed interface AddHoldingIntent : ViewIntent {
    data class SetCategory(val value: AssetCategory) : AddHoldingIntent
    data class SetCurrency(val value: Currency) : AddHoldingIntent
    data class SetName(val value: String) : AddHoldingIntent
    data class SetCode(val value: String) : AddHoldingIntent
    data class SetQuantity(val value: String) : AddHoldingIntent
    data class SetCurrentValue(val value: String) : AddHoldingIntent
    data class SetMaturityDate(val value: String) : AddHoldingIntent
    data class SetExcludedFromAllocation(val value: Boolean) : AddHoldingIntent
    object Submit : AddHoldingIntent
    object Dismiss : AddHoldingIntent
}

sealed interface AddHoldingEffect : ViewEffect {
    data class Committed(val name: String) : AddHoldingEffect
    object Close : AddHoldingEffect
    data class ShowError(val message: String) : AddHoldingEffect
}
