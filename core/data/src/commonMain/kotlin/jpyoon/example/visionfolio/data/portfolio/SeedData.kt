package jpyoon.example.visionfolio.data.portfolio

import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.Holding

object SeedData {
    val Initial: List<Holding> = listOf(
        Holding("samsung", AssetCategory.DOMESTIC_STOCK, "삼성전자", "005930",
            quantity = 500.0, currentValue = 37_900_000.0, currency = Currency.KRW),
        Holding("sk-hynix", AssetCategory.DOMESTIC_STOCK, "SK하이닉스", "000660",
            quantity = 50.0, currentValue = 10_275_000.0, currency = Currency.KRW),
        Holding("nvidia", AssetCategory.OVERSEAS_STOCK, "NVIDIA", "NVDA",
            quantity = 20.0, currentValue = 18_240.0, currency = Currency.USD),
        Holding("apple", AssetCategory.OVERSEAS_STOCK, "Apple", "AAPL",
            quantity = 30.0, currentValue = 6_420.0, currency = Currency.USD),
        Holding("tiger-sp500", AssetCategory.ETF, "TIGER 미국S&P500", "360750",
            quantity = 1000.0, currentValue = 19_180_000.0, currency = Currency.KRW),
        Holding("kodex-200", AssetCategory.ETF, "KODEX 200", "069500",
            quantity = 300.0, currentValue = 11_526_000.0, currency = Currency.KRW),
        Holding("btc", AssetCategory.CRYPTO, "비트코인", "BTC/KRW",
            quantity = 0.3, currentValue = 28_740_000.0, currency = Currency.KRW),
        Holding("eth", AssetCategory.CRYPTO, "이더리움", "ETH/KRW",
            quantity = 1.5, currentValue = 6_480_000.0, currency = Currency.KRW),
        Holding("kt-bond", AssetCategory.BOND, "국고채 10년", "KTB10Y",
            quantity = 1.0, currentValue = 5_120_000.0, currency = Currency.KRW,
            maturityDate = "2034-06-10"),
        Holding("kb-deposit", AssetCategory.CASH, "KB정기예금", "KB-D24",
            quantity = 1.0, currentValue = 5_210_000.0, currency = Currency.KRW),
        Holding("nh-pension", AssetCategory.PENSION, "NH 퇴직연금", "NH-IRP",
            quantity = 1.0, currentValue = 13_650_000.0, currency = Currency.KRW),
        Holding("shinhan-savings", AssetCategory.CASH, "신한 자유적금", "SH-F24",
            quantity = 1.0, currentValue = 3_120_000.0, currency = Currency.KRW),
        Holding("sol", AssetCategory.CRYPTO, "솔라나", "SOL/KRW",
            quantity = 30.0, currentValue = 7_215_000.0, currency = Currency.KRW),
    )
}
