package jpyoon.example.visionfolio.domain.model

enum class UploadStage { PICK, DOWNLOADING_MODEL, PARSING, REVIEW, DONE }

enum class ParsedField {
    NAME,
    CODE,
    QUANTITY,
    AVG_PRICE,
    CURRENT_PRICE,
    CATEGORY,
    MATURITY_DATE,
}

data class ScreenshotRef(
    val uri: String,
    val sizeBytes: Long,
)

data class ParsedHolding(
    val selected: Boolean = true,
    val category: AssetCategory,
    val name: String,
    val code: String,
    val quantity: Double,
    val avgPrice: Double,
    val currentPrice: Double,
    val currency: Currency,
    val confidence: Float,
    val maturityDate: String? = null,
)

data class UploadResult(
    val parsed: List<ParsedHolding>,
    val elapsedMs: Long,
)
