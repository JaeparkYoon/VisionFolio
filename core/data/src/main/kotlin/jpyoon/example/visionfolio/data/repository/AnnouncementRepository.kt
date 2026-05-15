package jpyoon.example.visionfolio.data.repository

import jpyoon.example.visionfolio.domain.model.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    fun observeAll(): Flow<List<Announcement>>
    fun observeHighlighted(): Flow<Announcement?>
}
