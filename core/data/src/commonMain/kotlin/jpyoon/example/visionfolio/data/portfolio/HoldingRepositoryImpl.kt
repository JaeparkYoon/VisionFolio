package jpyoon.example.visionfolio.data.portfolio

import jpyoon.example.visionfolio.core.common.di.AppCoroutineScope
import jpyoon.example.visionfolio.data.portfolio.db.holding.HoldingDao
import jpyoon.example.visionfolio.data.portfolio.db.holding.toDomain
import jpyoon.example.visionfolio.data.portfolio.db.holding.toEntity
import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

@AppSingleton
class HoldingRepositoryImpl @Inject constructor(
    private val dao: HoldingDao,
    @AppCoroutineScope private val appScope: CoroutineScope,
) : HoldingRepository {

    init {
        seedIfEmpty()
    }

    private fun seedIfEmpty() {
        appScope.launch(Dispatchers.IO) {
            if (dao.count() == 0) {
                dao.upsertAll(SeedData.Initial.map { it.toEntity() })
            }
        }
    }

    override fun observe(): Flow<List<Holding>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun addAll(holdings: List<Holding>) {
        dao.upsertAll(holdings.map { it.toEntity() })
    }

    override suspend fun remove(id: String) {
        dao.deleteById(id)
    }

    override suspend fun update(holding: Holding) {
        dao.update(holding.toEntity())
    }
}
