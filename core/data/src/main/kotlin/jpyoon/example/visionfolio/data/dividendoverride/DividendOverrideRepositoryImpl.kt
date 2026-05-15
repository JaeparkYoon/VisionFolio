package jpyoon.example.visionfolio.data.dividendoverride

import jpyoon.example.visionfolio.data.portfolio.db.dividendoverride.DividendOverrideDao
import jpyoon.example.visionfolio.data.portfolio.db.dividendoverride.toDomain
import jpyoon.example.visionfolio.data.portfolio.db.dividendoverride.toEntity
import jpyoon.example.visionfolio.data.repository.DividendOverrideRepository
import jpyoon.example.visionfolio.domain.model.DividendOverride
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DividendOverrideRepositoryImpl @Inject constructor(
    private val dao: DividendOverrideDao,
) : DividendOverrideRepository {

    override fun observeAll(): Flow<List<DividendOverride>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun upsert(override: DividendOverride) = dao.upsert(override.toEntity())

    override suspend fun delete(holdingId: String) = dao.deleteByHoldingId(holdingId)
}
