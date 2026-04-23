package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun observeToday(): Flow<Quote>
}
