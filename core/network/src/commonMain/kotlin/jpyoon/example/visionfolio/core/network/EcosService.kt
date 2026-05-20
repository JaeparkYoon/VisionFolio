package jpyoon.example.visionfolio.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 한국은행 ECOS Open API.
 * 통계표 731Y001 = 주요국 통화의 대원화환율, item code 0000001 = USD/KRW.
 * Returns mock data instead of real HTTP calls.
 */
class EcosService {
    suspend fun statisticSearch(
        apiKey: String,
        startDate: String,
        endDate: String,
        startNo: Int = 1,
        endNo: Int = 100,
        statCode: String = "731Y001",
        cycle: String = "D",
        itemCode: String = "0000001",
    ): EcosResponse = EcosResponse(
        statisticSearch = EcosSearch(
            listTotalCount = 1,
            row = listOf(
                EcosRow(
                    statCode = statCode,
                    itemCode1 = itemCode,
                    time = endDate,
                    dataValue = "1380.50",
                    unitName = "원",
                ),
            ),
        ),
    )
}

@Serializable
data class EcosResponse(
    @SerialName("StatisticSearch") val statisticSearch: EcosSearch? = null,
    @SerialName("RESULT") val result: EcosResult? = null,
)

@Serializable
data class EcosSearch(
    @SerialName("list_total_count") val listTotalCount: Int = 0,
    val row: List<EcosRow>? = null,
)

@Serializable
data class EcosRow(
    @SerialName("STAT_CODE") val statCode: String? = null,
    @SerialName("ITEM_CODE1") val itemCode1: String? = null,
    @SerialName("TIME") val time: String? = null,
    @SerialName("DATA_VALUE") val dataValue: String? = null,
    @SerialName("UNIT_NAME") val unitName: String? = null,
)

@Serializable
data class EcosResult(
    @SerialName("CODE") val code: String? = null,
    @SerialName("MESSAGE") val message: String? = null,
)
