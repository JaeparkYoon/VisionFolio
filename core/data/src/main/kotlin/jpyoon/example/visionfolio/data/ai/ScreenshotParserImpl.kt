package jpyoon.example.visionfolio.data.ai

import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.domain.model.ParsedHolding
import jpyoon.example.visionfolio.domain.model.ScreenshotRef
import jpyoon.example.visionfolio.domain.model.UploadResult
import jpyoon.example.visionfolio.data.repository.HoldingRepository
import jpyoon.example.visionfolio.data.repository.ScreenshotParser
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenshotParserImpl @Inject constructor(
    private val holdingRepository: HoldingRepository,
) : ScreenshotParser {

    override suspend fun parse(
        shots: List<ScreenshotRef>,
        onDownloadProgress: ((Float) -> Unit)?,
        onQuotaExceeded: ((Int, Int) -> Unit)?,
    ): UploadResult {
        val start = System.currentTimeMillis()
        delay(1_600)
        val parsed = parsedFromLocalDb(shots.size)
        return UploadResult(
            parsed = parsed,
            elapsedMs = System.currentTimeMillis() - start,
        )
    }

    private suspend fun parsedFromLocalDb(shotCount: Int): List<ParsedHolding> {
        val holdings = holdingRepository.observe().first()
        if (holdings.isEmpty()) return emptyList()
        val take = (shotCount * 2).coerceIn(1, holdings.size)
        return holdings.take(take).map { it.toParsed() }
    }

    private fun Holding.toParsed(): ParsedHolding = ParsedHolding(
        category = category,
        name = name,
        code = code,
        quantity = quantity,
        currentValue = currentValue,
        currency = currency,
        confidence = DEFAULT_CONFIDENCE,
    )

    private companion object {
        const val DEFAULT_CONFIDENCE = 0.95f
    }
}
