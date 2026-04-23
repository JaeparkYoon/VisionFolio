package jpyoon.example.visionfolio.feature.addholding

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jpyoon.example.visionfolio.core.android.MVIViewModel
import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.domain.model.isBond
import jpyoon.example.visionfolio.domain.model.isCash
import jpyoon.example.visionfolio.data.repository.HoldingRepository
import jpyoon.example.visionfolio.domain.usecase.AddHolding
import jpyoon.example.visionfolio.domain.usecase.HoldingDraft
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddHoldingViewModel @Inject constructor(
    private val addHolding: AddHolding,
    private val holdingRepo: HoldingRepository,
) : MVIViewModel<AddHoldingIntent, AddHoldingState, AddHoldingEffect>() {

    override fun createInitialState(): AddHoldingState = AddHoldingState()

    fun initForEdit(holding: Holding) {
        setState {
            copy(
                editingId = holding.id,
                category = holding.category,
                currency = holding.currency,
                name = holding.name,
                code = holding.code,
                quantity = if (holding.quantity % 1.0 == 0.0) holding.quantity.toLong().toString() else holding.quantity.toString(),
                avgPrice = if (holding.avgPrice % 1.0 == 0.0) holding.avgPrice.toLong().toString() else holding.avgPrice.toString(),
                currentPrice = if (holding.currentPrice % 1.0 == 0.0) holding.currentPrice.toLong().toString() else holding.currentPrice.toString(),
                maturityDate = holding.maturityDate.orEmpty(),
            )
        }
    }

    override suspend fun processIntent(intent: AddHoldingIntent) {
        when (intent) {
            is AddHoldingIntent.SetCategory -> setState { copy(category = intent.value) }
            is AddHoldingIntent.SetCurrency -> setState { copy(currency = intent.value) }
            is AddHoldingIntent.SetName -> setState { copy(name = intent.value, error = null) }
            is AddHoldingIntent.SetCode -> setState { copy(code = intent.value) }
            is AddHoldingIntent.SetQuantity -> setState { copy(quantity = intent.value.filterDouble()) }
            is AddHoldingIntent.SetAvgPrice -> setState { copy(avgPrice = intent.value.filterDouble()) }
            is AddHoldingIntent.SetCurrentPrice -> setState { copy(currentPrice = intent.value.filterDouble()) }
            is AddHoldingIntent.SetMaturityDate -> setState { copy(maturityDate = intent.value) }
            AddHoldingIntent.Submit -> submit()
            AddHoldingIntent.Dismiss -> setEffect { AddHoldingEffect.Close }
        }
    }

    private fun submit() {
        val snapshot = currentState
        if (!snapshot.canSubmit) return
        setState { copy(isSubmitting = true, error = null) }
        viewModelScope.launch {
            val isCash = snapshot.category.isCash
            val isBond = snapshot.category.isBond
            val resolvedQuantity = when {
                isCash -> snapshot.quantity.toDouble()
                isBond -> 1.0
                else -> snapshot.quantity.toDouble()
            }
            val resolvedAvgPrice = if (isCash) 1.0 else snapshot.avgPrice.toDouble()
            val resolvedCurrentPrice = if (isCash) 1.0 else snapshot.currentPrice.toDouble()
            val resolvedCode = if (isCash) "" else snapshot.code
            val resolvedMaturityDate = if (isBond) snapshot.maturityDate.ifBlank { null } else null
            if (snapshot.editingId != null) {
                runCatching {
                    holdingRepo.update(
                        Holding(
                            id = snapshot.editingId,
                            category = snapshot.category,
                            name = snapshot.name,
                            code = resolvedCode,
                            quantity = resolvedQuantity,
                            avgPrice = resolvedAvgPrice,
                            currentPrice = resolvedCurrentPrice,
                            currency = snapshot.currency,
                            maturityDate = resolvedMaturityDate,
                        ),
                    )
                }.onSuccess {
                    setEffect { AddHoldingEffect.Committed(snapshot.name) }
                    setEffect { AddHoldingEffect.Close }
                }.onFailure { t ->
                    setState { copy(isSubmitting = false, error = t.message) }
                    setEffect { AddHoldingEffect.ShowError(t.message ?: "수정 실패") }
                }
            } else {
                runCatching {
                    addHolding(
                        HoldingDraft(
                            category = snapshot.category,
                            name = snapshot.name,
                            code = resolvedCode,
                            quantity = resolvedQuantity,
                            avgPrice = resolvedAvgPrice,
                            currentPrice = resolvedCurrentPrice,
                            currency = snapshot.currency,
                            maturityDate = resolvedMaturityDate,
                        ),
                    )
                }.onSuccess { added ->
                    setEffect { AddHoldingEffect.Committed(added.name) }
                    setEffect { AddHoldingEffect.Close }
                }.onFailure { t ->
                    setState { copy(isSubmitting = false, error = t.message) }
                    setEffect { AddHoldingEffect.ShowError(t.message ?: "추가 실패") }
                }
            }
        }
    }
}

private fun String.filterDouble(): String = filter { it.isDigit() || it == '.' }
    .let { raw ->
        val firstDot = raw.indexOf('.')
        if (firstDot < 0) raw
        else raw.substring(0, firstDot + 1) + raw.substring(firstDot + 1).replace(".", "")
    }
