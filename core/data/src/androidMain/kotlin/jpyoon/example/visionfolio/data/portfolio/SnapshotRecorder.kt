package jpyoon.example.visionfolio.data.portfolio

import jpyoon.example.visionfolio.core.common.di.AppCoroutineScope
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.SnapshotDao
import jpyoon.example.visionfolio.data.portfolio.db.snapshot.snapshotEntityOf
import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.core.repository.api.FxRateProvider
import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import jpyoon.example.visionfolio.domain.model.compute.EnrichHoldings
import jpyoon.example.visionfolio.domain.model.compute.GetPortfolioSummary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.Instant
import java.time.ZoneId
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

@AppSingleton
class SnapshotRecorder @Inject constructor(
    private val holdingRepo: HoldingRepository,
    private val fxProvider: FxRateProvider,
    private val snapshotDao: SnapshotDao,
    @AppCoroutineScope private val appScope: CoroutineScope,
) {

    private val zone: ZoneId = ZoneId.systemDefault()

    fun start(scope: CoroutineScope = appScope) {
        holdingRepo.observe()
            .distinctUntilChanged()
            .onEach { holdings -> record(holdings) }
            .launchIn(scope)
    }

    private suspend fun record(holdings: List<Holding>) {
        if (holdings.isEmpty()) return
        val usdKrw = runCatching { fxProvider.usdToKrw() }.getOrDefault(1_388.0)
        val enriched = EnrichHoldings.invoke(holdings, usdKrw)
        val summary = GetPortfolioSummary.invoke(enriched)
        val dayStart = Instant.now()
            .atZone(zone)
            .toLocalDate()
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()
        snapshotDao.upsert(
            snapshotEntityOf(
                timestamp = dayStart,
                totalKrw = summary.totalValue,
                byCategory = summary.byCategory,
            ),
        )
    }
}
