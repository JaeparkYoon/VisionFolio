<!-- Banner -->
<p align="center">
  <img src="play-store-icon-512.png" alt="VisionFolio" width="120" />
</p>

<h1 align="center">VisionFolio <sub><sup>(가칭)</sup></sub></h1>

<p align="center">
  <b>스크린샷 한 장으로 흩어진 투자 자산을 모으다.</b><br/>
  100% AI 바이브 코딩 창작물 · 오픈소스 샘플 버전
</p>

<p align="center">
  <img alt="platform" src="https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white" />
  <img alt="kotlin"   src="https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white" />
  <img alt="compose"  src="https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?logo=jetpackcompose&logoColor=white" />
  <img alt="minSdk"   src="https://img.shields.io/badge/minSdk-28-555" />
  <img alt="license"  src="https://img.shields.io/badge/license-Apache_2.0-black" />
</p>

---

## 💡 왜 만들었나

AI시대에 개발자가 비즈니스 역량을 키우고, 바이브코딩을 통한 0 to 1 앱의 최종 결과물은 어떨지 확인하고 싶었습니다.

> ℹ️ **오픈소스 버전 안내**
> 공개 저장소는 **네트워크 호출 없이 100% mock 데이터**로만 동작합니다.
> - 스크린샷 업로드 UI는 그대로 유지되지만, 파싱 결과는 로컬 DB 기반 샘플로 채워집니다.
> - 시장 지수 · 환율 · 뉴스 · 배당은 모두 번들된 mock 데이터를 반환합니다.
> - Gemini / Firebase AI · NewsAPI · Financial Modeling Prep · 공공데이터포털 연동 코드는 모두 제거되어 있습니다.

---

## ✨ 핵심 기능

| | |
| --- | --- |
| 📸 **스크린샷 업로드 UI** | 다중 스크린샷 선택 → 파싱 흐름 · 검수 편집기 · 커밋까지 이어지는 MVI 기반 플로우 |
| 🏠 **홈** | 총자산 + 오늘/총수익 + 1개월 미니차트 / 카테고리 비중 / 보유 종목 / 시장지수 · 환율 · 뉴스 카드 |
| 📈 **자산 추이** | 1일~전체 8개 구간, 드래그 hover 차트, 카테고리별 기여도 바, 스냅샷 기반 기간 요약 |
| 💰 **배당** | 보유 종목별 배당 이력 (mock 데이터) |
| ⚙️ **설정** | 금액 숨김, 백업/내보내기, 알림, 프로필 |

---

## 🎨 디자인 시스템 (요약)

**따뜻한 미니멀 · 라이트 전용**

| Token | Hex |
| --- | --- |
| Background | `#FFFFFF` |
| Card Soft | `#FBF7F2` |
| Ink | `#1F1B17` |
| **Accent (코랄)** | `#E88A7A` |
| Accent Ink | `#C06051` |
| Accent Wash | `#FCEAE4` |

- **Font**: Pretendard Variable
- **Radius**: 10 / 14 / 20 / 28
- **Spacing**: 4dp grid
- **Icon**: 21종, 1.6~2.0 stroke

---

## 🧱 아키텍처

```
presentation (Compose + MVI)
    │  State ◀── ViewModel ──▶ Effect
    ▼
domain (model · formatter · usecase)
    ▼
data (repo · prefs · mock)
```

- **멀티모듈**: `app` · `core:*` · `feature:*` · `designsystem` · `build-logic`
- **UI**: Jetpack Compose · Material 3
- **State**: MVI (State / Intent / Effect)
- **DI**: Hilt
- **Persist**: Room (DB) + DataStore Preferences
- **Nav**: Navigation-Compose
- **Screenshot Parser**: `ScreenshotParser` 인터페이스만 유지 — 오픈소스 버전에서는 로컬 DB 샘플을 반환하는 `ScreenshotParserImpl`이 바인딩됨

---

## 🚀 시작하기

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

## 📄 License

Apache License 2.0 © 2026 VisionFolio
