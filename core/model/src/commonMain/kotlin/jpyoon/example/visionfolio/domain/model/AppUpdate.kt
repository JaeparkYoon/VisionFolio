package jpyoon.example.visionfolio.domain.model

data class AppUpdate(val latestVersion: String, val forceUpdate: Boolean = false, val updateUrl: String = "")
