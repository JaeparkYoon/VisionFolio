package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.credit.CreditBalance
import kotlinx.coroutines.flow.Flow

interface CreditRepository {
    fun observeBalance(): Flow<CreditBalance>
    suspend fun tryDeduct(amount: Int): Boolean
    suspend fun grant(amount: Int)
    suspend fun refund(amount: Int)

    companion object {
        const val INITIAL_FREE_CREDITS = 30
    }
}
