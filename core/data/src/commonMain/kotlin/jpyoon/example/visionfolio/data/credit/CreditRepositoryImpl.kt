package jpyoon.example.visionfolio.data.credit

import jpyoon.example.visionfolio.core.repository.api.CreditRepository
import jpyoon.example.visionfolio.domain.model.credit.CreditBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.tatarka.inject.annotations.Inject

@Inject
class CreditRepositoryImpl : CreditRepository {
    private val _balance = MutableStateFlow(CreditBalance(remaining = CreditRepository.INITIAL_FREE_CREDITS, firstGrantDone = true))

    override fun observeBalance(): Flow<CreditBalance> = _balance

    override suspend fun deduct(amount: Int): Boolean {
        val current = _balance.value
        if (current.remaining < amount) return false
        _balance.value = current.copy(remaining = current.remaining - amount)
        return true
    }

    override suspend fun grantInitialIfNeeded() {
        // Mock: already granted
    }
}
