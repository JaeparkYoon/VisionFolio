package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.credit.CreditBalance
import jpyoon.example.visionfolio.domain.model.credit.CreditPack
import jpyoon.example.visionfolio.domain.model.credit.PurchaseResult
import kotlinx.coroutines.flow.Flow

interface CreditRepository {
    companion object { const val INITIAL_FREE_CREDITS = 30 }
    fun observeBalance(): Flow<CreditBalance>
    suspend fun deduct(amount: Int): Boolean
    suspend fun grantInitialIfNeeded()
}
