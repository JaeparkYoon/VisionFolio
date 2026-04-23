package jpyoon.example.visionfolio.feature.upload.event

import jpyoon.example.visionfolio.core.analytics.event.Events

sealed interface UploadEvents : Events {
    data object ViewedUpload : UploadEvents
}
