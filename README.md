<!-- Banner -->
<p align="center">
  <img src="play-store-icon-512.png" alt="VisionFolio" width="120" />
</p>

<h1 align="center">VisionFolio </h1>

<p align="center">
  <b>스크린샷 한 장으로 흩어진 투자 자산을 모으다.</b><br/>
  Kotlin Multiplatform &middot; 100% AI 바이브 코딩 창작물 &middot; 오픈소스 샘플 버전
</p>

<p align="center">
  <img alt="platform" src="https://img.shields.io/badge/platform-Android_|_iOS-3DDC84?logo=kotlin&logoColor=white" />
  <img alt="kotlin" src="https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white" />
  <img alt="compose" src="https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?logo=jetpackcompose&logoColor=white" />
  <img alt="swiftui" src="https://img.shields.io/badge/SwiftUI-5-F05138?logo=swift&logoColor=white" />
  <img alt="minSdk" src="https://img.shields.io/badge/minSdk-26-555" />
  <img alt="license" src="https://img.shields.io/badge/license-Apache_2.0-black" />
</p>

---

## 왜 만들었나

AI 시대에 개발자가 비즈니스 역량을 키우고, 바이브코딩을 통한 0 to 1 앱의 최종 결과물은 어떨지 확인하고 싶었습니다.

> **오픈소스 버전 안내**
> 공개 저장소는 **네트워크 호출 없이 100% mock 데이터**로만 동작합니다.
> - 스크린샷 업로드 UI는 그대로 유지되지만, 파싱 결과는 로컬 DB 기반 샘플로 채워집니다.
> - 시장 지수 · 환율 · 뉴스 · 배당은 모두 번들된 mock 데이터를 반환합니다.
> - Gemini / Firebase AI · NewsAPI · Financial Modeling Prep · 공공데이터포털 연동 코드는 모두 제거되어 있습니다.

---

## 핵심 기능

| 기능 | 설명 |
|------|------|
| **스크린샷 업로드 UI** | 최대 5장을 한 번에 업로드 &rarr; 검수 화면에서 편집 후 확정 (mock 파싱) |
| **홈 대시보드** | 총자산, 일간 변동, YTD 수익률, 카테고리별 비중, 보유 종목 목록, 배당금 요약 |
| **AI 포트폴리오 채팅** | 대화형 포트폴리오 분석 · 질문 · 조언 (mock AI 응답) |
| **자산 추이** | 1일~전체 8개 구간 차트, 드래그 hover, 카테고리별 기여도, 커스텀 범위 |
| **수익률 추적** | 연간 수익 기록 (배당, 매매, 이자, 기타), 베이스라인 편집 |
| **배당 &middot; 시장 인사이트** | 종목별 배당금 예측, 시장 지수, 투자 구루 프로필, 뉴스 요약 (mock 데이터) |
| **설정** | 금액 숨김, CSV 백업/복원, 알림, 자산 편집, 프로필 |

---

## 기술 스택

### 공통 (KMP)

| 분류 | 기술 |
|------|------|
| 언어 | Kotlin 2.2.10 (Multiplatform) |
| 아키텍처 | MVI (Model-View-Intent) &middot; Unidirectional Data Flow |
| DI | kotlin-inject 0.9.0 (KSP 기반, 런타임 리플렉션 없음) |
| DB | Room 2.7.1 KMP (SQLite Bundled) |
| 네트워크 | Ktor 3.0.3 + Kotlinx Serialization 1.7.3 |
| 설정 저장 | DataStore Preferences 1.1.1 |
| 날짜/시간 | Kotlinx Datetime 0.7.1 |

### Android

| 분류 | 기술 |
|------|------|
| UI | Jetpack Compose &middot; Material 3 (BOM 2024.09.00) |
| AGP | 9.1.1 &middot; compileSdk 36 &middot; minSdk 26 |
| 네트워크 엔진 | Ktor OkHttp |
| 이미지 | Coil 2.7.0 |
| 백그라운드 | WorkManager 2.10.0 (일간 요약 알림) |

### iOS

| 분류 | 기술 |
|------|------|
| UI | SwiftUI 5 |
| 네트워크 엔진 | Ktor Darwin |
| KMP 브릿지 | MVIViewModelWrapper (StateFlow &rarr; @Published) |

### 백엔드 (Mock)

| 분류 | 기술 |
|------|------|
| 런타임 | Firebase Cloud Functions &middot; Node 22 &middot; TypeScript 6.0 |
| 함수 | 3개 (claimWelcomeBonus, consumeCsGrant, verifyPurchase) |
| 동작 | 모든 함수가 mock 성공 응답 반환 |

---

## 프로젝트 구조

```
VisionFolio/
├── androidApp/              # Android 앱 모듈 (Activity, DI, Navigation)
├── iosApp/                  # iOS 앱 (SwiftUI 셸)
├── shared/                  # iOS용 KMP 브릿지 (IosAppComponent, Repository stubs)
├── core/
│   ├── common-kotlin/       # MVIViewModel, PlatformViewModel, DI 어노테이션
│   ├── model/               # 도메인 모델 (Holding, Currency, ChatMessage, ...)
│   ├── domain/              # UseCase 계층 (Observe, Commit, Validate, ...)
│   ├── data/                # Repository 구현, AI 파서, Room DB, mock 서비스
│   ├── repository-api/      # Repository 인터페이스 (레이어 분리)
│   ├── network/             # Ktor 네트워크 서비스 (mock 데이터 반환)
│   ├── model-resources/     # Android 리소스 매핑 (AssetCategory → @StringRes)
│   ├── navigation/          # NavRoutes, TopDestination
│   └── analytics/           # 이벤트 트래킹
├── designsystem/            # VfColors, VfDialog, Pretendard, 커스텀 컴포넌트
├── feature/
│   ├── home/                # 홈 대시보드 (총자산, 비중, 배당, YTD)
│   ├── upload/              # 스크린샷 업로드 + Foreground Service
│   ├── chat/                # AI 채팅 (mock 응답)
│   ├── trend/               # 자산 추이 차트
│   ├── dividend/            # 배당 · 시장 인사이트
│   ├── returns/             # 수익률 추적
│   ├── settings/            # 설정
│   ├── addholding/          # 수동 자산 추가 · 편집
│   └── tweaks/              # 개발 유틸리티
├── functions/               # Firebase Cloud Functions (3개 mock 함수)
├── build-logic/             # Gradle Convention Plugins
└── gradle/                  # Version Catalog (libs.versions.toml)
```

---

## 아키텍처

[Android Architecture Guide](https://developer.android.com/topic/architecture)를 기반으로 UI Layer &rarr; Domain Layer &rarr; Data Layer 3계층 구조를 따릅니다.

```
┌─────────────────────────────────────────────────────────┐
│  UI Layer                                               │
│  feature/{home,upload,chat,trend,dividend,returns,...}   │
│  ┌──────────┐  ┌──────────┐  ┌───────┐  ┌──────────┐   │
│  │ Contract │→ │ViewModel │→ │ Route │→ │  Screen  │   │
│  │(State/   │  │(MVI      │  │(Side  │  │(Compose/ │   │
│  │ Intent/  │  │ 처리)     │  │Effect)│  │ SwiftUI) │   │
│  │ Effect)  │  │          │  │       │  │          │   │
│  └──────────┘  └──────────┘  └───────┘  └──────────┘   │
├─────────────────────────────────────────────────────────┤
│  Domain Layer                                           │
│  core/domain/ — UseCase (17+)                           │
│  core/model/  — 도메인 모델 (순수 Kotlin, 플랫폼 의존성 없음)  │
├─────────────────────────────────────────────────────────┤
│  Data Layer                                             │
│  core/repository-api/ — Repository 인터페이스 (14개)       │
│  core/data/           — Repository 구현체 (mock)          │
│  ┌────────┐ ┌──────┐ ┌──────────┐ ┌─────────┐          │
│  │Room KMP│ │ Ktor │ │Mock 서비스│ │DataStore│          │
│  │(로컬DB) │ │(구조) │ │(샘플 데이터)│ │ (설정)   │          │
│  └────────┘ └──────┘ └──────────┘ └─────────┘          │
└─────────────────────────────────────────────────────────┘
```

### UI Layer (`feature/`)

각 Feature 모듈은 MVI(Model-View-Intent) 패턴으로 **단방향 데이터 흐름**을 구현합니다.

| 파일 | 역할 |
|------|------|
| `{Feature}Contract.kt` | `State` (data class), `Intent` (sealed interface), `Effect` (sealed interface) 정의 |
| `{Feature}ViewModel.kt` | `MVIViewModel` 상속. Intent 처리, State 갱신, Effect 발행 |
| `{Feature}Route.kt` | Side-effect 처리 (`LaunchedEffect`, `BackHandler`, Effect 수신 &rarr; 네비게이션) |
| `{Feature}Screen.kt` | 순수 UI Composable. State를 받아 렌더링, 사용자 액션을 Intent로 디스패치 |

**데이터 흐름**: `Screen` &rarr; `Intent` &rarr; `ViewModel.processIntent()` &rarr; `setState { copy(…) }` &rarr; `StateFlow` &rarr; 리컴포지션

**일회성 이벤트**: `ViewModel` &rarr; `setEffect()` &rarr; `Channel<Effect>` &rarr; Route에서 네비게이션/Toast 등 처리

**iOS 브릿지**: `MVIViewModelWrapper`가 KMP `StateFlow`를 Swift `@Published`로 변환합니다.

### Domain Layer (`core/domain/`, `core/model/`)

**순수 Kotlin**으로 작성되며 Android/iOS 플랫폼 의존성이 없습니다. Repository 인터페이스에만 의존합니다.

| UseCase | 역할 |
|---------|------|
| `ObserveHomeDataUseCase` | 보유자산 + 시세 + 환율 + 설정을 결합하여 `HomeData` Flow 제공 |
| `CommitParsedHoldingsUseCase` | AI 파싱 결과를 포트폴리오에 병합 (코드 기반 중복 제거) |
| `SendChatMessageUseCase` | 크레딧 차감 &rarr; mock AI 응답 &rarr; 에러 시 자동 환불 |
| `ObserveTrendDataUseCase` | 포트폴리오 시계열 + 구간 통계 + 카테고리 기여도 결합 |
| `ObserveDividendDataUseCase` | 보유자산 + 배당 데이터 + 사용자 오버라이드로 `DividendSummary` 산출 |
| `GetYtdSummaryUseCase` | 수익 기록 + 베이스라인 + 환율로 YTD 요약 계산 |
| `ExportHoldingsCsvUseCase` | 보유자산 CSV 내보내기 |
| 그 외 | 채팅 세션 CRUD, 수익 기록 CRUD, CSV 가져오기 등 |

도메인 모델(`core/model/`): `Holding`, `EnrichedHolding`, `PortfolioSummary`, `ChatSession`, `ChatMessage`, `ReturnEntry`, `YtdSummary`, `CreditBalance` 등

### Data Layer (`core/repository-api/`, `core/data/`)

**인터페이스와 구현의 완전한 분리**: `repository-api`에 인터페이스만 정의하고, `core/data`에서 구현합니다. Feature 모듈은 인터페이스에만 의존하며 구현체를 알지 못합니다.

| Repository | 데이터 소스 |
|------------|------------|
| `HoldingRepository` | Room KMP (`holdings` 테이블) |
| `SeriesRepository` | Room KMP (`portfolio_snapshots`) |
| `ChatRepository` | Room KMP (mock AI 응답) |
| `CreditRepository` | DataStore (로컬 잔액 관리) |
| `DividendRepository` | Room 캐시 + mock 배당 데이터 |
| `MarketRepository` | mock 서비스 (시장 지수, 환율, 뉴스) |
| `AppPrefsRepository` | DataStore Preferences |
| `AnnouncementRepository` | 하드코딩 샘플 |
| `ScreenshotParser` | 로컬 DB 기반 샘플 반환 |
| `QuoteRepository`, `ReturnEntryRepository` 등 | Room KMP |

**DI 바인딩**: kotlin-inject `AppComponent`에서 `@Provides @AppSingleton`으로 인터페이스 &harr; 구현체를 연결합니다.

---

## Cloud Functions (Mock)

| 함수 | 용도 |
|------|------|
| `claimWelcomeBonus` | 신규 유저 보너스 크레딧 지급 (mock 성공 응답) |
| `consumeCsGrant` | CS 수동 지급 크레딧 소비 (mock 성공 응답) |
| `verifyPurchase` | Android/iOS 인앱 구매 서버 검증 (mock 성공 응답) |

---

## 디자인 시스템

**폰트**: Pretendard Variable &middot; **Radius**: 10 / 14 / 20 / 28dp &middot; **Grid**: 4dp

### 테마

라이트 전용.

| 토큰 | Hex |
|------|-----|
| Background | `#FFFFFF` |
| Card Soft | `#FBF7F2` |
| Ink Primary | `#1F1B17` |
| Ink Secondary | `#5E544B` |
| Accent (코랄) | `#E88A7A` |
| Accent Ink | `#C06051` |
| Accent Wash | `#FCEAE4` |

---

## 프라이버시

- 오픈소스 버전은 **외부 네트워크 호출 없이** 동작합니다
- 핵심 데이터는 **로컬 Room DB** 저장 (오프라인 우선)
- API 키, 시크릿, google-services.json 등 민감 정보가 코드에 포함되어 있지 않습니다

---

## 빌드

### 필수 환경

- Android Studio Narwhal 이상
- JDK 17+ (Android Studio 번들 JBR 권장)

### Android

```bash
./gradlew :androidApp:assembleDebug
```

### iOS Shared Framework

```bash
./gradlew :shared:compileKotlinIosSimulatorArm64
```

Xcode에서 `iosApp/VisionFolio.xcodeproj`를 열고 빌드합니다.

### Cloud Functions

```bash
cd functions && npm install && npm run build
```

별도의 API 키 설정은 필요하지 않습니다. 앱은 번들된 mock 데이터만으로 동작합니다.

---

## License

Apache License 2.0 © 2026 VisionFolio
