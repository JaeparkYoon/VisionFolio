package jpyoon.example.visionfolio.domain.model

enum class AssetCategory {
    DOMESTIC_STOCK,
    OVERSEAS_STOCK,
    ETF,
    CRYPTO,
    BOND,
    CASH,
    PENSION,
    SAVINGS,
    /** 기타 — 사용자가 직접 추가하는 자유 항목. 이름 + 금액만 필수. */
    OTHER,
}

val AssetCategory.isCash: Boolean get() = this == AssetCategory.CASH
val AssetCategory.isBond: Boolean get() = this == AssetCategory.BOND
val AssetCategory.isSavings: Boolean get() = this == AssetCategory.SAVINGS
val AssetCategory.isPension: Boolean get() = this == AssetCategory.PENSION
val AssetCategory.isOther: Boolean get() = this == AssetCategory.OTHER

val AssetCategory.displayName: String get() = when (this) {
    AssetCategory.DOMESTIC_STOCK -> "국내주식"
    AssetCategory.OVERSEAS_STOCK -> "해외주식"
    AssetCategory.ETF -> "ETF"
    AssetCategory.CRYPTO -> "암호화폐"
    AssetCategory.BOND -> "채권"
    AssetCategory.CASH -> "현금"
    AssetCategory.PENSION -> "연금"
    AssetCategory.SAVINGS -> "저축"
    AssetCategory.OTHER -> "기타"
}

enum class Currency { KRW, USD }

data class Holding(
    val id: String,
    val category: AssetCategory,
    val name: String,
    val code: String,
    val quantity: Double,
    /**
     * 현재 평가금액(자산 native currency 기준 합계). 실시간 시세를 받지 않고 스크린샷 캡처
     * 시점의 평가금액 스냅샷을 그대로 보존한다. 가격이 움직이면 사용자가 새 스크린샷을
     * 다시 업로드하는 모델.
     */
    val currentValue: Double,
    val currency: Currency,
    val maturityDate: String? = null,
    val source: String = "",
    val sector: Sector? = null,
    /**
     * true 면 자산 배분 도넛/범례에서 제외. 총자산·KPI·배당 예측·AI 노출에는 영향 없음.
     * 현금/예금 등 비중 시각화에서 빼고 싶을 때 사용.
     */
    val excludedFromAllocation: Boolean = false,
)

data class EnrichedHolding(
    val holding: Holding,
    val valueKrw: Long,
)

data class PortfolioSummary(
    val totalValue: Long,
    val byCategory: Map<AssetCategory, Long>,
    /** 오늘 자정 이전 가장 최근 스냅샷 대비 변동 금액 (KRW). 비교 기준이 없으면 0. */
    val dayChange: Long = 0,
    /** dayChange의 백분율. 비교 기준이 없으면 0. */
    val dayPct: Double = 0.0,
    /** 변동 계산의 기준이 된 스냅샷 timestamp (ms). 없으면 0 → UI에서 변동 줄을 숨김. */
    val prevTimestamp: Long = 0,
)
