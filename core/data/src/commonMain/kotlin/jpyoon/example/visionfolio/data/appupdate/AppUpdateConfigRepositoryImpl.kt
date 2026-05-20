package jpyoon.example.visionfolio.data.appupdate

import jpyoon.example.visionfolio.core.repository.api.AppUpdateConfigRepository
import jpyoon.example.visionfolio.domain.model.AppUpdate
import me.tatarka.inject.annotations.Inject

@Inject
class AppUpdateConfigRepositoryImpl : AppUpdateConfigRepository {
    override suspend fun getUpdateConfig(): AppUpdate = AppUpdate(latestVersion = "1.0.0", forceUpdate = false)
}
