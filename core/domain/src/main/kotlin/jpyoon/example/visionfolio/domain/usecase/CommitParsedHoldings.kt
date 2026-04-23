package jpyoon.example.visionfolio.domain.usecase

import jpyoon.example.visionfolio.domain.model.AssetCategory
import jpyoon.example.visionfolio.domain.model.Holding
import jpyoon.example.visionfolio.domain.model.ParsedHolding
import jpyoon.example.visionfolio.data.repository.HoldingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CommitParsedHoldings @Inject constructor(
    private val holdingRepo: HoldingRepository,
) {

    suspend operator fun invoke(parsed: List<ParsedHolding>, source: String = ""): Int {
        val approved = parsed.filter { it.selected }
        val existingHoldings = holdingRepo.observe().first()
        val existingBonds = existingHoldings
            .filter { it.category == AssetCategory.BOND && !it.maturityDate.isNullOrBlank() }

        // 같은 source 로 이전에 들여온 항목은 먼저 제거 (source 내부 추가/삭제 반영)
        if (source.isNotBlank()) {
            existingHoldings.filter { it.source == source }.forEach {
                holdingRepo.remove(it.id)
            }
        }

        // source 삭제 후 살아남은 항목 기준으로 content(code 우선, 없으면 category|currency|name) 기반 2차 dedup.
        // 스크롤·상단 변화로 지문이 흔들려 다른 라벨이 할당돼도 같은 종목이면 기존 id 를 재사용해 덮어쓴다.
        val surviving = if (source.isNotBlank()) {
            existingHoldings.filterNot { it.source == source }
        } else {
            existingHoldings
        }
        val byCode = surviving.filter { it.code.isNotBlank() }.associateBy { it.code }
        val byNameKey = surviving.filter { it.code.isBlank() }
            .associateBy { "${it.category}|${it.currency}|${it.name}" }

        val holdings = approved.map { p ->
            val maturityDate = if (p.category == AssetCategory.BOND && p.maturityDate.isNullOrBlank()) {
                existingBonds.find { it.name == p.name || (it.code.isNotBlank() && it.code == p.code) }?.maturityDate
            } else {
                p.maturityDate
            }

            val contentMatchId = if (p.code.isNotBlank()) {
                byCode[p.code]?.id
            } else {
                byNameKey["${p.category}|${p.currency}|${p.name}"]?.id
            }
            val id = contentMatchId ?: newImportedId(source, p)

            Holding(
                id = id,
                category = p.category,
                name = p.name,
                code = p.code,
                quantity = p.quantity,
                avgPrice = p.avgPrice,
                currentPrice = p.currentPrice,
                currency = p.currency,
                maturityDate = maturityDate,
                source = source,
            )
        }
        holdingRepo.addAll(holdings)
        return holdings.size
    }

    private fun newImportedId(source: String, p: ParsedHolding): String = when {
        source.isNotBlank() && p.code.isNotBlank() -> "imported-$source-${p.code}"
        source.isNotBlank() -> "imported-$source-${p.category}-${p.currency}-${p.name}"
        else -> "imported-${java.util.UUID.randomUUID()}"
    }
}
