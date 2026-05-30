# 릴리스 & Play Store 자동 배포 가이드

## 브랜치 전략

| 브랜치      | 용도                                                                                  |
| ----------- | ------------------------------------------------------------------------------------- |
| `develop`   | 개발 기본 브랜치. 모든 기능 작업은 여기서 분기·병합.                                   |
| `Release/*` | 릴리스 준비 브랜치. (예: `Release/1.5.0`)                                              |
| `main`      | 배포 브랜치. **여기에 머지되면 자동으로 Play Store에 출시**되고 태그(`X.Y.Z`)가 찍힘. |

> ⚠️ 현재 원격(origin)에는 `main` 브랜치가 없습니다. 자동 배포가 동작하려면 `main` 브랜치를
> 먼저 만들어야 합니다. (아래 "최초 1회 셋업 → 0) main 브랜치 생성" 참고)

## 자동 배포 흐름

```
Release/1.5.0 ──(PR 머지)──▶ main
                                │
                                ▼
          .github/workflows/release.yml 실행
                                │
        ┌───────────────────────┼───────────────────────┐
        ▼                       ▼                       ▼
  서명된 .aab 빌드      Play Store(production) 업로드   main에 1.5.0 태그 푸시
```

- 트리거: **`Release/*` 브랜치에서 `main`으로 보낸 PR이 "머지"될 때만** 실행됩니다.
  (PR을 머지 없이 닫기만 하면 실행되지 않습니다.)
- 빌드/업로드 같은 비가역 작업 전에 버전 검증 단계가 먼저 실행되어, 버전이 잘못됐으면 **빠르게 실패**합니다.
- 수동 재실행이 필요하면 GitHub Actions 탭에서 `Release to Play Store` 워크플로우를 `workflow_dispatch`로 실행할 수 있습니다.

---

## ⚠️ 릴리스 전 필수 체크: versionCode / versionName 올리기

Play Store는 **업로드할 때마다 `versionCode`가 직전보다 커야** 합니다. (같으면 업로드 거부)
또한 현재 `1.4.0`(versionCode `10400`)은 **이미 출시되어 동일 이름의 태그가 존재**하므로,
다음 릴리스에서는 반드시 값을 올려야 합니다.

`Release/*` 브랜치에서 PR을 만들기 전에 [`gradle/libs.versions.toml`](../gradle/libs.versions.toml)을 수정하세요.

```toml
[versions]
versionCode = "10500"   # 직전 값(10400)보다 크게
versionName = "1.5.0"   # 태그는 1.5.0 으로 생성됨 (접두사 v 없음)
```

> 만약 값을 올리지 않으면, 워크플로우의 버전 검증 단계가 "태그가 이미 존재한다"며
> 빌드/업로드 이전에 즉시 실패합니다. (프로덕션에 중복 업로드가 발생하지 않음)

---

## 최초 1회 셋업

### 0) `main` 브랜치 생성 (origin에 아직 없음)

현재 배포 기준이 되는 `main` 브랜치가 원격에 없습니다. 보통 가장 최근 릴리스 시점(현재 `develop` = `1.4.0`)에서 만듭니다.

```bash
git fetch origin
git switch -c main origin/develop
git push -u origin main
```

> 개발은 계속 `develop`에서 진행하고, 릴리스할 때만 `Release/* → main` PR을 보냅니다.
> (GitHub 저장소의 기본 브랜치는 `develop`로 유지해도 됩니다. 원하면 `main`에 브랜치 보호 규칙을 거세요.)

### GitHub Secrets 등록

자동 배포가 동작하려면 아래 Secrets를 모두 등록해야 합니다.
**저장소 → Settings → Secrets and variables → Actions → New repository secret**

| Secret 이름                 | 값                                       | 상태      |
| --------------------------- | ---------------------------------------- | --------- |
| `GOOGLE_SERVICES_JSON`      | `app/google-services.json` 의 base64     | 이미 있음 |
| `LOCAL_PROPERTIES_CONTENTS` | `local.properties` 내용                  | 이미 있음 |
| `KEYSTORE_BASE64`           | 릴리스 키스토어(`.jks`)의 base64         | **추가**  |
| `KEYSTORE_PASSWORD`         | 키스토어 비밀번호                        | **추가**  |
| `KEY_PASSWORD`              | 키 비밀번호                              | **추가**  |
| `KEY_ALIAS`                 | 키 별칭(alias)                           | **추가**  |
| `PLAY_SERVICE_ACCOUNT_JSON` | Google Play 서비스 계정 JSON 키 전체 내용 | **추가**  |

> 🔐 `KEYSTORE_PASSWORD`, `KEY_PASSWORD` 값은 **이 문서에 적지 마세요.** 팀에서 공유 중인
> 키스토어 비밀번호(비밀번호 관리자/사내 보안 채널 참조)를 GitHub Secrets에만 입력합니다.
> 비밀번호가 외부에 노출된 적이 있다면 **업로드 키 재설정/비밀번호 교체**를 권장합니다.

### 1) 키스토어 관련 시크릿

릴리스 서명에 쓰는 `.jks` 파일을 base64로 변환해 `KEYSTORE_BASE64`에 넣습니다. (macOS)

```bash
base64 -i /path/to/release.jks | pbcopy   # 클립보드에 복사됨 → 그대로 붙여넣기
```

> 워크플로우는 `base64 -d`로 복원하며, 줄바꿈이 포함된 base64도 정상 디코딩됩니다.

키 별칭(alias)을 모르면 아래로 확인합니다.

```bash
keytool -list -v -keystore /path/to/release.jks
# "Alias name: ..." 줄의 값이 KEY_ALIAS 입니다.
```

### 2) Google Play 서비스 계정 JSON 만들기 (아직 없음 → 1회 설정)

Play Store에 API로 업로드하려면 권한이 연결된 서비스 계정 키가 필요합니다.

1. **Play Console** → **설정 → API 액세스(API access)** 진입
2. **새 서비스 계정 만들기** 안내를 따라 **Google Cloud Console**로 이동
   - 해당 GCP 프로젝트에서 **서비스 계정 생성** (이름 예: `play-publisher`)
   - 생성 후 **키 → 키 추가 → 새 키 만들기 → JSON** 선택 → JSON 파일 다운로드
3. Google Cloud Console에서 **Google Play Android Developer API**가 사용 설정(Enabled)되어 있는지 확인
4. 다시 **Play Console → API 액세스**로 돌아와 방금 만든 서비스 계정에 **권한 부여(Grant access)**
   - 앱 권한: `com.teamhy2.hongikyeolgong2` 선택
   - 계정 권한: **"프로덕션 트랙으로 출시"** 및 **"앱 정보 보기"** 포함
5. 다운로드한 **JSON 파일 전체 내용**을 `PLAY_SERVICE_ACCOUNT_JSON` 시크릿에 붙여넣기

> 권한 반영에는 최대 24시간이 걸릴 수 있습니다.

### 3) Play App Signing / 첫 업로드 주의사항

- 앱이 **Play App Signing**을 사용 중이라면 위 `.jks`는 *업로드 키*입니다. (이미 1.4.0을 올렸다면 보통 활성화되어 있음)
- `1.4.0`이 이미 프로덕션에 출시되어 있으므로 "draft 앱" 류의 첫 업로드 제약은 발생하지 않습니다.
- 첫 자동 배포를 안전하게 검증하고 싶다면, `.github/workflows/release.yml`의 `track: production`을
  `track: internal`로 잠시 바꿔 내부 테스트로 한 번 돌려본 뒤 되돌리는 것을 권장합니다.

---

## 로컬에서 서명된 빌드를 만들고 싶다면

`local.properties`에 아래 두 줄을 추가하세요. (이 파일은 git에 올라가지 않습니다.)

```properties
storeFile=/absolute/path/to/release.jks
keyAlias=여기에_키_별칭
# storePassword, keyPassword 는 이미 local.properties에 있음
```

그 후:

```bash
./gradlew :app:bundleRelease   # 결과: app/build/outputs/bundle/release/app-release.aab
```

> 빌드 설정은 환경 변수(`KEYSTORE_FILE`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`)를 우선 사용하고,
> 없으면 `local.properties`를 사용합니다. 키스토어가 전혀 없으면 release는 서명 없이 빌드되어
> 기존 CI(`android.yml`의 빌드/테스트/ktlint)는 영향받지 않습니다.

---

## 단계적 출시(staged rollout)로 바꾸기

`release.yml`의 업로드 스텝을 아래처럼 변경하면 일부 사용자에게만 먼저 배포됩니다.

```yaml
track: production
status: inProgress
userFraction: 0.1 # 10%부터 시작
```
