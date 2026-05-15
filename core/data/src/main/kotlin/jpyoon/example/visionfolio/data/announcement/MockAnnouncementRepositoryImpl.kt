package jpyoon.example.visionfolio.data.announcement

import jpyoon.example.visionfolio.data.repository.AnnouncementRepository
import jpyoon.example.visionfolio.domain.model.Announcement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAnnouncementRepositoryImpl @Inject constructor() : AnnouncementRepository {

    private val announcements = listOf(
        Announcement(
            id = "1",
            title = "VisionFolio 2.0 업데이트 안내",
            body = "다크모드, 수익 관리, AI 채팅 기능이 추가되었습니다. 업데이트 후 새로운 기능을 확인해 보세요!",
            isHighlighted = true,
        ),
        Announcement(
            id = "2",
            title = "적금/기타 자산 카테고리 추가",
            body = "이제 적금과 기타 자산도 포트폴리오에 추가할 수 있습니다.",
        ),
        Announcement(
            id = "3",
            title = "스크린샷 인식 정확도 향상",
            body = "증권사 앱 스크린샷 인식률이 크게 개선되었습니다.",
        ),
    )

    override fun observeAll(): Flow<List<Announcement>> = flowOf(announcements)

    override fun observeHighlighted(): Flow<Announcement?> =
        flowOf(announcements.firstOrNull { it.isHighlighted })
}
