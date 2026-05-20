package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.AppUpdate

interface AppUpdateConfigRepository {
    suspend fun getUpdateConfig(): AppUpdate
}
