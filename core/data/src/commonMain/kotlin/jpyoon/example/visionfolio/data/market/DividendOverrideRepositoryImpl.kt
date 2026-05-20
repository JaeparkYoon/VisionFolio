package jpyoon.example.visionfolio.data.market

import jpyoon.example.visionfolio.core.repository.api.DividendOverrideRepository
import jpyoon.example.visionfolio.domain.model.DividendOverride
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.tatarka.inject.annotations.Inject

@Inject
class DividendOverrideRepositoryImpl : DividendOverrideRepository {
    private val overrides = MutableStateFlow<List<DividendOverride>>(emptyList())

    override fun observeAll(): Flow<List<DividendOverride>> = overrides

    override suspend fun upsert(override: DividendOverride) {
        overrides.value = overrides.value.filter { it.holdingId != override.holdingId } + override
    }

    override suspend fun delete(holdingId: String) {
        overrides.value = overrides.value.filter { it.holdingId != holdingId }
    }
}
