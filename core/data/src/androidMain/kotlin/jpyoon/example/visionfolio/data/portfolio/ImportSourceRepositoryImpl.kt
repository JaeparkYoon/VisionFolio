package jpyoon.example.visionfolio.data.portfolio

import jpyoon.example.visionfolio.data.fingerprint.ScreenshotFingerprinter
import jpyoon.example.visionfolio.data.portfolio.db.importsource.ImportSourceDao
import jpyoon.example.visionfolio.data.portfolio.db.importsource.ImportSourceEntity
import jpyoon.example.visionfolio.core.repository.api.ImportSourceRepository
import me.tatarka.inject.annotations.Inject
import jpyoon.example.visionfolio.core.common.di.AppSingleton

@AppSingleton
class ImportSourceRepositoryImpl @Inject constructor(
    private val dao: ImportSourceDao,
    private val fingerprinter: ScreenshotFingerprinter,
) : ImportSourceRepository {

    override suspend fun resolveLabel(fingerprint: Long): String {
        val existing = dao.getAll()
        val match = existing.firstOrNull { fingerprinter.isSimilar(it.fingerprint, fingerprint) }
        if (match != null) {
            dao.upsert(match.copy(fingerprint = fingerprint))
            return match.label
        }
        val nextLabel = nextLabel(existing.map { it.label })
        dao.upsert(ImportSourceEntity(label = nextLabel, fingerprint = fingerprint))
        return nextLabel
    }

    private fun nextLabel(usedLabels: List<String>): String {
        val used = usedLabels.toSet()
        var ch = 'A'
        while (used.contains(ch.toString())) ch++
        return ch.toString()
    }
}
