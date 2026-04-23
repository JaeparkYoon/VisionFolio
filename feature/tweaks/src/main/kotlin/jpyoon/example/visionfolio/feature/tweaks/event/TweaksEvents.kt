package jpyoon.example.visionfolio.feature.tweaks.event

import jpyoon.example.visionfolio.core.analytics.event.Events

sealed interface TweaksEvents : Events {
    data object ViewedTweaks : TweaksEvents
}
