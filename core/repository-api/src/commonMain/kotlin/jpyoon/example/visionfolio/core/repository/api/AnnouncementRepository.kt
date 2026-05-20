package jpyoon.example.visionfolio.core.repository.api

import jpyoon.example.visionfolio.domain.model.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun observeAnnouncements(): Flow<List<Announcement>>
}
