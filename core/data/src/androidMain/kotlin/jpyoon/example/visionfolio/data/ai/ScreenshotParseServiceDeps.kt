package jpyoon.example.visionfolio.data.ai

import jpyoon.example.visionfolio.core.repository.api.ScreenshotParser

/**
 * Interface that the Application class must implement so that
 * [ScreenshotParseService] can obtain its dependencies without
 * referencing the app module directly.
 */
interface ScreenshotParseServiceDeps {
    val screenshotParser: ScreenshotParser
    val screenshotParseSession: ScreenshotParseSession
    val screenshotParseResultStore: ScreenshotParseResultStore
}
