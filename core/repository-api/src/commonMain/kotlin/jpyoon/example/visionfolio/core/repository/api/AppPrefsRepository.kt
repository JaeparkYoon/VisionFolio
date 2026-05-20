package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.AccentPreset
import jpyoon.example.visionfolio.domain.model.AppPrefs
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.NotificationKey
import jpyoon.example.visionfolio.domain.model.NotificationPrefs
import jpyoon.example.visionfolio.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface AppPrefsRepository {
    fun observePrefs(): Flow<AppPrefs>
    fun observeNotifications(): Flow<NotificationPrefs>
    suspend fun setAccent(preset: AccentPreset)
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setHideAmounts(hide: Boolean)
    suspend fun setDisplayCurrency(currency: Currency)
    suspend fun toggleNotification(key: NotificationKey)

    /** POST_NOTIFICATIONS 거절 누적 횟수. 3회 이상 → 시스템 다이얼로그 대신 앱 설정으로 직진. */
    fun observeNotifDenialCount(): Flow<Int>
    suspend fun incrementNotifDenialCount()
    suspend fun resetNotifDenialCount()

    /** 익명 사용자 식별자. 최초 호출 시 생성·영속화되며 이후 동일 값 emit. */
    fun observeUserUuid(): Flow<String>

    /** 업로드 화면 스크린샷 가이드 다이얼로그를 다시 보지 않음으로 표시. */
    fun observeScreenshotGuideHidden(): Flow<Boolean>
    suspend fun setScreenshotGuideHidden(hidden: Boolean)

    /**
     * 연초 자산 baseline (KRW 환산). 수익률 화면의 연간 YTD% 계산 기준.
     * 연도별 따로 저장. 미설정 시 null.
     */
    fun observeReturnBaseline(year: Int): Flow<Long?>
    suspend fun setReturnBaseline(year: Int, krw: Long?)
}
