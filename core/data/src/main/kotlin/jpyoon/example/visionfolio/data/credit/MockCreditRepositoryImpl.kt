package jpyoon.example.visionfolio.data.credit

import jpyoon.example.visionfolio.data.repository.CreditRepository
import jpyoon.example.visionfolio.domain.model.credit.CreditBalance
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockCreditRepositoryImpl @Inject constructor() : CreditRepository {

    private val balanceFlow = MutableStateFlow(CreditBalance(remaining = 999, total = 999))

    override fun observeBalance(): Flow<CreditBalance> = balanceFlow

    override suspend fun tryDeduct(amount: Int): Boolean = true

    override suspend fun grant(amount: Int) { /* no-op */ }

    override suspend fun refund(amount: Int) { /* no-op */ }
}
