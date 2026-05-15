<!-- Banner -->
<p align="center">
  <img src="play-store-icon-512.png" alt="VisionFolio" width="120" />
</p>

<h1 align="center">VisionFolio</h1>

<p align="center">
  <b>스크린샷 한 장으로 흩어진 투자 자산을 모으다.</b><br/>
  100% AI 바이브 코딩 창작물 · 오픈소스 샘플 버전
</p>

<p align="center">
  <img alt="platform" src="https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white" />
  <img alt="kotlin"   src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white" />
  <img alt="compose"  src="https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?logo=jetpackcompose&logoColor=white" />
  <img alt="minSdk"   src="https://img.shields.io/badge/minSdk-28-555" />
  <img alt="license"  src="https://img.shields.io/badge/license-MIT-black" />
</p>

---

## 왜 만들었나

AI시대에 개발자가 비즈니스 역량을 키우고, 바이브코딩을 통한 0 to 1 앱의 최종 결과물은 어떨지 확인하고 싶었습니다.

> **오픈소스 버전 안내**
> 공개 저장소는 **네트워크 호출 없이 100% mock 데이터**로만 동작합니다.
> - 스크린샷 업로드 UI는 그대로 유지되지만, 파싱 결과는 로컬 DB 기반 샘플로 채워집니다.
> - AI 채팅은 mock 스트리밍 응답을 반환합니다. 크레딧은 항상 충분합니다.
> - 시장 지수 · 환율 · 뉴스 · 배당은 모두 번들된 mock 데이터를 반환합니다.
> - Gemini / Firebase AI · NewsAPI · Financial Modeling Prep · 공공데이터포털 연동 코드는 모두 제거되어 있습니다.

---

## 핵심 기능

| 기능 | 설명 |
| --- | --- |
| **스크린샷 업로드** | 다중 스크린샷 선택 → 파싱 → 검수 편집기 → 커밋까지 이어지는 MVI 기반 플로우 |
| **홈** | 총자산 + 일간 변동(dayChange/dayPct) + 카테고리 비중 + 보유 종목 + 시장지수 · 환율 · 뉴스 |
| **자산 추이** | 1일~전체 8개 구간, 드래그 hover 차트, 카테고리별 기여도 바, 스냅샷 기반 기간 요약 |
| **AI 채팅** | AI 기반 투자 상담 (mock 스트리밍 응답), 세션 관리, 모델 선택 |
| **수익 관리** | 연간 수익 기록, 월별 엔트리 CRUD, YTD 요약 |
| **배당** | 보유 종목별 배당 이력, 연간/분기/월간 배당 합계 |
| **종목 추가/관리** | 9개 자산 카테고리, 스냅샷 기반 평가금액(currentValue) 입력, 배분 제외 설정 |
| **설정** | 테마 모드(System/Light/Dark), 금액 숨김, 통화 전환, 백업/내보내기, 알림, 프로필 |
| **다크모드** | 전체 앱 다크모드 대응 (SYSTEM/LIGHT/DARK) |

---

## 자산 카테고리

| 카테고리 | 표시명 |
| --- | --- |
| `DOMESTIC_STOCK` | 국내주식 |
| `OVERSEAS_STOCK` | 해외주식 |
| `ETF` | ETF |
| `CRYPTO` | 암호화폐 |
| `BOND` | 채권 |
| `CASH` | 예적금 |
| `PENSION` | 연금/IRP |
| `SAVINGS` | 적금 |
| `OTHER` | 기타 |

---

## 아키텍처

```
presentation (Compose + MVI)
    │  State ◀── ViewModel ──▶ Effect
    ▼
domain (model · compute · usecase)
    ▼
data (repository · prefs · mock)
    ▼
local (Room DB · DataStore)
```

### 기술 스택

| 영역 | 기술 |
| --- | --- |
| UI | Jetpack Compose · Material 3 |
| State | MVI (State / Intent / Effect) |
| DI | Hilt |
| DB | Room (v2, destructive migration) |
| Prefs | DataStore Preferences |
| Nav | Navigation-Compose |
| 멀티모듈 | `app` · `core:*` · `feature:*` · `designsystem` · `build-logic` |
| 테마 | Light / Dark / System (ThemeMode) |

### 데이터 모델

스냅샷 기반 평가금액 모델을 사용합니다:

- **Holding**: `currentValue` (평가금액) 단일 필드 — `avgPrice`/`currentPrice` 대신 스냅샷 시점의 총 평가금액을 저장
- **EnrichedHolding**: `holding` + `valueKrw` — P/L 계산 없이 환율 변환된 평가금액만 보유
- **PortfolioSummary**: `dayChange`/`dayPct` 기반 일간 변동, `excludedFromAllocation` 필터 적용

### 주요 Use Cases

| Use Case | 역할 |
| --- | --- |
| `ObserveHomeData` | 홈 대시보드 데이터 스트림 (holdings + quote + summary) |
| `CommitParsedHoldings` | 파싱된 스크린샷 결과를 DB에 커밋 |
| `AddHolding` | 수동 종목 추가/수정 |
| `ExportHoldingsCsv` | CSV 내보내기 |
| `SendChatMessageUseCase` | AI 채팅 메시지 전송 (mock) |
| `ObserveReturnsUseCase` | 연간 수익 데이터 관찰 |
| `GetYtdSummaryUseCase` | YTD 수익 요약 계산 |

### 주요 Repositories

| Repository | 구현체 | 설명 |
| --- | --- | --- |
| `HoldingRepository` | Room 기반 | 보유 종목 CRUD |
| `ChatRepository` | `MockChatRepositoryImpl` | AI 채팅 (mock 스트리밍) |
| `ReturnEntryRepository` | `MockReturnEntryRepositoryImpl` | 수익 기록 CRUD |
| `CreditRepository` | `MockCreditRepositoryImpl` | 크레딧 (항상 충분) |
| `AnnouncementRepository` | `MockAnnouncementRepositoryImpl` | 공지사항 (하드코딩) |
| `AppPrefsRepository` | DataStore 기반 | 앱 설정 (테마, 통화, 알림 등) |

---

## 디자인 시스템

**따뜻한 미니멀 · 라이트/다크 대응**

| Token | Light | Dark |
| --- | --- | --- |
| Background | `#FFFFFF` | `#0E0C0A` |
| Card | `#FFFFFF` | `#14110E` |
| Ink Primary | `#1F1B17` | `#F5EFE7` |
| Accent (코랄) | `#E88A7A` | `#ED9D8E` |
| Up | `#B55D4B` | `#D98574` |
| Down | `#4A6B7A` | `#7F9DAC` |

- **Font**: Pretendard Variable
- **Radius**: 10 / 14 / 20 / 28
- **Spacing**: 4dp grid
- **Accent Presets**: Salmon, Terracotta, Coral, Burgundy

---

## 시작하기

### 필수 환경

- Android Studio Narwhal 이상
- JDK 17+ (Android Studio 번들 JBR 권장)
- Android SDK 28 (minSdk) 이상

### 빌드

```bash
./gradlew :app:assembleDebug
```

별도의 API 키 설정은 필요하지 않습니다. 앱은 번들된 mock 데이터만으로 동작합니다.

---

## License

MIT © 2026 VisionFolio
