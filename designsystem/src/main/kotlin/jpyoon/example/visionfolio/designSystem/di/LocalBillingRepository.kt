package jpyoon.example.visionfolio.designsystem.di

import androidx.compose.runtime.staticCompositionLocalOf
import jpyoon.example.visionfolio.core.repository.api.BillingRepository

val LocalBillingRepository = staticCompositionLocalOf<BillingRepository> {
    error("LocalBillingRepository not provided. Wrap setContent with CompositionLocalProvider.")
}
