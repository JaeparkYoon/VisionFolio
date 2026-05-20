package jpyoon.example.visionfolio.data.announcement

import jpyoon.example.visionfolio.core.repository.api.AnnouncementRepository
import jpyoon.example.visionfolio.domain.model.Announcement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject

@Inject
class AnnouncementRepositoryImpl : AnnouncementRepository {
    override fun observeAnnouncements(): Flow<List<Announcement>> = flowOf(
        listOf(
            Announcement(id = "1", title = "VisionFolio 데모", body = "이것은 데모 버전입니다.", date = "2024-01-01"),
        )
    )
}
