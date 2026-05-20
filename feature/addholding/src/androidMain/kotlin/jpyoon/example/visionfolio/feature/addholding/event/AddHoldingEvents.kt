package jpyoon.example.visionfolio.feature.addholding.event

import jpyoon.example.visionfolio.core.analytics.event.Events

sealed interface AddHoldingEvents : Events {
    data object ViewedAddHolding : AddHoldingEvents
}
