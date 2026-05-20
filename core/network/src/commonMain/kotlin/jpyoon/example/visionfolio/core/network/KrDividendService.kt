package jpyoon.example.visionfolio.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 공공데이터포털 -- 주식배당정보 API.
 * Returns mock data instead of real HTTP calls.
 */
class KrDividendService {
    suspend fun getDividendInfo(
        serviceKey: String,
        numOfRows: Int = 300,
        pageNo: Int = 1,
        resultType: String = "json",
        companyName: String? = null,
        itemName: String? = null,
    ): KrDividendResponse = KrDividendResponse(
        response = KrDividendResponseBody(
            header = KrDividendHeader(resultCode = "00", resultMsg = "NORMAL SERVICE."),
            body = KrDividendBodyContent(
                items = KrDividendItems(
                    item = listOf(
                        KrDividendItem(
                            baseDate = "20250115",
                            itemName = "삼성전자",
                            companyName = "삼성전자(주)",
                            isinCode = "KR7005930003",
                            dividendAmount = "1444",
                            dividendRate = "2.1",
                            paymentDate = "20250415",
                            dividendBaseDate = "20241231",
                        ),
                    ),
                ),
                totalCount = 1,
            ),
        ),
    )
}

class KrDividendApiException(message: String) : Exception(message)

@Serializable
data class KrDividendResponse(
    val response: KrDividendResponseBody = KrDividendResponseBody(),
)

@Serializable
data class KrDividendResponseBody(
    val header: KrDividendHeader = KrDividendHeader(),
    val body: KrDividendBodyContent = KrDividendBodyContent(),
)

@Serializable
data class KrDividendHeader(
    val resultCode: String = "",
    val resultMsg: String = "",
)

@Serializable
data class KrDividendBodyContent(
    val items: KrDividendItems = KrDividendItems(),
    val totalCount: Int = 0,
)

@Serializable
data class KrDividendItems(
    val item: List<KrDividendItem> = emptyList(),
)

@Serializable
data class KrDividendItem(
    @SerialName("basDt") val baseDate: String = "",
    @SerialName("itmsNm") val itemName: String = "",
    @SerialName("stckIssuCmpyNm") val companyName: String = "",
    @SerialName("isinCd") val isinCode: String = "",
    @SerialName("stckGenrDvdnAmt") val dividendAmount: String = "0",
    @SerialName("stckGenrCashDvdnRt") val dividendRate: String = "0",
    @SerialName("cashDvdnPayDt") val paymentDate: String = "",
    @SerialName("dvdnBasDt") val dividendBaseDate: String = "",
)
