package jpyoon.example.visionfolio.shared

import jpyoon.example.visionfolio.core.repository.api.AppPrefsRepository
import jpyoon.example.visionfolio.core.repository.api.DividendOverrideRepository
import jpyoon.example.visionfolio.core.repository.api.HoldingRepository
import jpyoon.example.visionfolio.core.repository.api.MarketRepository
import jpyoon.example.visionfolio.domain.usecase.AddHolding
import jpyoon.example.visionfolio.domain.usecase.CommitParsedHoldings
import jpyoon.example.visionfolio.domain.usecase.ExportHoldingsCsv
import jpyoon.example.visionfolio.domain.usecase.ObserveHomeData

/**
 * Swift entry point for accessing the KMP DI graph.
 *
 * Usage from Swift:
 * ```swift
 * let graph = IosAppGraphKt.createIosAppGraph()
 * let holdings = graph.holdingRepository.observe()
 * ```
 */
class IosAppGraph(private val component: IosAppComponent) {

    // -- Repositories --
    val holdingRepository: HoldingRepository = component.holdingRepository
    val marketRepository: MarketRepository = component.marketRepository
    val appPrefsRepository: AppPrefsRepository = component.appPrefsRepository
    val dividendOverrideRepository: DividendOverrideRepository = component.dividendOverrideRepository

    // -- UseCases --
    val addHolding: AddHolding = component.addHolding
    val exportHoldingsCsv: ExportHoldingsCsv = component.exportHoldingsCsv
    val commitParsedHoldings: CommitParsedHoldings = component.commitParsedHoldings
    val observeHomeData: ObserveHomeData = component.observeHomeData
}

/**
 * Each iOS target source set provides an actual for this expect declaration.
 * KSP generates target-specific `IosAppComponent::class.create()`.
 */
internal expect fun createIosAppComponentInstance(): IosAppComponent

fun createIosAppGraph(): IosAppGraph = IosAppGraph(createIosAppComponentInstance())
