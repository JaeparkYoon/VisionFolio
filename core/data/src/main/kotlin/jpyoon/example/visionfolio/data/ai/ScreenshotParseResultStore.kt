@file:OptIn(kotlinx.serialization.InternalSerializationApi::class, kotlinx.serialization.ExperimentalSerializationApi::class)

package jpyoon.example.visionfolio.data.ai

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.ParsedHolding
import jpyoon.example.visionfolio.domain.model.UploadResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Foreground Service 가 프로세스 생존을 보장하지 못하는 구간 (파싱 직후 stopSelf → 시스템이 프로세스 정리)
 * 에도 결과가 살아남도록 파싱 결과를 디스크에 영속화한다. ViewModel 은 init 에서 [consume] 으로 한 번만 회수한다.
 */
@Singleton
class ScreenshotParseResultStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {

    private val mutex = Mutex()
    private val file: File by lazy { File(context.filesDir, FILE_NAME) }
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun save(result: UploadResult): Unit = withContext(Dispatchers.IO) {
        mutex.withLock {
            runCatching {
                file.writeText(json.encodeToString(result.toDto()))
            }.onFailure { Log.w(TAG, "save 실패", it) }
        }
    }

    /** 저장된 결과가 있으면 반환하고 파일을 즉시 삭제한다. */
    suspend fun consume(): UploadResult? = withContext(Dispatchers.IO) {
        mutex.withLock {
            if (!file.exists()) return@withLock null
            runCatching {
                val text = file.readText()
                file.delete()
                json.decodeFromString<UploadResultDto>(text).toDomain()
            }.onFailure {
                Log.w(TAG, "consume 실패", it)
                file.delete()
            }.getOrNull()
        }
    }

    suspend fun clear(): Unit = withContext(Dispatchers.IO) {
        mutex.withLock {
            runCatching { if (file.exists()) file.delete() }
        }
    }

    private companion object {
        const val TAG = "ParseResultStore"
        const val FILE_NAME = "screenshot_parse_result.json"
    }
}

@Serializable
private data class UploadResultDto(
    val parsed: List<ParsedHoldingDto>,
    val elapsedMs: Long,
)

@Serializable
private data class ParsedHoldingDto(
    val selected: Boolean,
    val category: String,
    val name: String,
    val code: String,
    val quantity: Double,
    val currentValue: Double,
    val currency: String,
    val confidence: Float,
    val maturityDate: String? = null,
)

private fun UploadResult.toDto(): UploadResultDto = UploadResultDto(
    parsed = parsed.map { it.toDto() },
    elapsedMs = elapsedMs,
)

private fun ParsedHolding.toDto(): ParsedHoldingDto = ParsedHoldingDto(
    selected = selected,
    category = category.name,
    name = name,
    code = code,
    quantity = quantity,
    currentValue = currentValue,
    currency = currency.name,
    confidence = confidence,
    maturityDate = maturityDate,
)

private fun UploadResultDto.toDomain(): UploadResult = UploadResult(
    parsed = parsed.mapNotNull { it.toDomainOrNull() },
    elapsedMs = elapsedMs,
)

private fun ParsedHoldingDto.toDomainOrNull(): ParsedHolding? {
    val cat = runCatching { AssetCategory.valueOf(category) }.getOrNull() ?: return null
    val cur = runCatching { Currency.valueOf(currency) }.getOrNull() ?: return null
    return ParsedHolding(
        selected = selected,
        category = cat,
        name = name,
        code = code,
        quantity = quantity,
        currentValue = currentValue,
        currency = cur,
        confidence = confidence,
        maturityDate = maturityDate,
    )
}
