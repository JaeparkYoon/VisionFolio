package jpyoon.example.visionfolio.data.market

import jpyoon.example.visionfolio.domain.model.Quote
import jpyoon.example.visionfolio.core.repository.api.QuoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

@AppSingleton
class QuoteRepositoryImpl @Inject constructor() : QuoteRepository {

    private val pool = listOf(
        Quote("가격은 당신이 내는 것이고, 가치는 당신이 얻는 것이다.", "워런 버핏"),
        Quote("위험은 당신이 무엇을 하고 있는지 모를 때 생긴다.", "워런 버핏"),
        Quote("시장에서는 인내가 가장 중요한 무기다.", "워런 버핏"),
    )

    override fun observeToday(): Flow<Quote> = flowOf(pool.first())
}
