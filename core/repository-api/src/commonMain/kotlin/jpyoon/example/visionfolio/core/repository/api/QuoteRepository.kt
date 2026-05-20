package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.Quote
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun observeToday(): Flow<Quote>
}
