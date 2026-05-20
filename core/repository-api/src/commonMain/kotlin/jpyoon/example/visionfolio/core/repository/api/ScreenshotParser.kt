package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.ScreenshotRef
import jpyoon.example.visionfolio.domain.model.UploadResult

/**
 * @param onDownloadProgress 모델 다운로드 진행률 콜백 (0f ~ 1f).
 *        온디바이스 모델 다운로드가 필요한 경우에만 호출됨.
 * @param onQuotaExceeded API 사용량 초과 시 호출되는 콜백. (현재 시도 횟수, 최대 재시도 횟수)
 */
interface ScreenshotParser {
    suspend fun parse(
        shots: List<ScreenshotRef>,
        onDownloadProgress: ((Float) -> Unit)? = null,
        onQuotaExceeded: ((attempt: Int, maxRetries: Int) -> Unit)? = null,
    ): UploadResult
}
