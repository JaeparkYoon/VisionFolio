package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.DividendRecord
import jpyoon.example.visionfolio.domain.model.Holding

interface DividendRepository {
    suspend fun getDividends(holding: Holding): List<DividendRecord>
    suspend fun clearCache()
}
