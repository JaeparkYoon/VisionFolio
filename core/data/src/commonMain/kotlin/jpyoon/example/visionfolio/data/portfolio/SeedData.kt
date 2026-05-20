package jpyoon.example.visionfolio.data.portfolio

import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Currency
import jpyoon.example.visionfolio.domain.model.Holding

object SeedData {
    val Initial: List<Holding> = listOf(
        Holding("samsung", AssetCategory.DOMESTIC_STOCK, "삼성전자", "005930",
            quantity = 40_000_000.0, avgPrice = 68_000.0, currentPrice = 75_800.0, currency = Currency.KRW),
        Holding("sk-hynix", AssetCategory.DOMESTIC_STOCK, "SK하이닉스", "000660",
            quantity = 9_000_000.0, avgPrice = 128_000.0, currentPrice = 205_500.0, currency = Currency.KRW),
        Holding("nvidia", AssetCategory.OVERSEAS_STOCK, "NVIDIA", "NVDA",
            quantity = 2_670_000.0, avgPrice = 420.0, currentPrice = 912.0, currency = Currency.USD),
        Holding("apple", AssetCategory.OVERSEAS_STOCK, "Apple", "AAPL",
            quantity = 5_560_000.0, avgPrice = 150.0, currentPrice = 214.0, currency = Currency.USD),
        Holding("tiger-sp500", AssetCategory.ETF, "TIGER 미국S&P500", "360750",
            quantity = 20_000_000.0, avgPrice = 14_200.0, currentPrice = 19_180.0, currency = Currency.KRW),
        Holding("kodex-200", AssetCategory.ETF, "KODEX 200", "069500",
            quantity = 11_130_000.0, avgPrice = 33_800.0, currentPrice = 38_420.0, currency = Currency.KRW),
        Holding("btc", AssetCategory.CRYPTO, "비트코인", "BTC/KRW",
            quantity = 26_700.0, avgPrice = 42_000_000.0, currentPrice = 95_800_000.0, currency = Currency.KRW),
        Holding("eth", AssetCategory.CRYPTO, "이더리움", "ETH/KRW",
            quantity = 400_000.0, avgPrice = 2_900_000.0, currentPrice = 4_320_000.0, currency = Currency.KRW),
        Holding("kt-bond", AssetCategory.BOND, "국고채 10년", "KTB10Y",
            quantity = 222_500.0, avgPrice = 5_000_000.0, currentPrice = 5_120_000.0, currency = Currency.KRW,
            maturityDate = "2034-06-10"),
        Holding("kb-deposit", AssetCategory.CASH, "KB정기예금", "KB-D24",
            quantity = 222_500.0, avgPrice = 5_000_000.0, currentPrice = 5_210_000.0, currency = Currency.KRW),
        Holding("nh-pension", AssetCategory.PENSION, "NH 퇴직연금", "NH-IRP",
            quantity = 222_500.0, avgPrice = 12_000_000.0, currentPrice = 13_650_000.0, currency = Currency.KRW),
        Holding("shinhan-savings", AssetCategory.CASH, "신한 자유적금", "SH-F24",
            quantity = 222_500.0, avgPrice = 3_000_000.0, currentPrice = 3_120_000.0, currency = Currency.KRW),
        Holding("sol", AssetCategory.CRYPTO, "솔라나", "SOL/KRW",
            quantity = 4_000_000.0, avgPrice = 120_000.0, currentPrice = 240_500.0, currency = Currency.KRW),
    )
}
