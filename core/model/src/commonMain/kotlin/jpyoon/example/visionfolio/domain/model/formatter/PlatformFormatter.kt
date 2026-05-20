package jpyoon.example.visionfolio.domain.model.formatter

/**
 * 천 단위 콤마 그룹핑. 예: 1234567 → "1,234,567"
 */
expect fun formatGrouped(value: Long): String

/**
 * 천 단위 콤마 + 소수점 한 자리. 예: 1234.5678 → "1,234.6"
 */
expect fun formatGroupedOneDecimal(value: Double): String

/**
 * 소수점 두 자리 고정. 예: 12.3 → "12.30"
 */
expect fun formatTwoDecimal(value: Double): String

/**
 * 천 단위 콤마 + 소수점 두 자리 고정. 예: 1234.5 → "1,234.50"
 * USD처럼 cents까지 표시해야 하는 통화 본문 포매팅에 사용.
 */
expect fun formatGroupedTwoDecimal(value: Double): String
