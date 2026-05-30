---
name: release
description: >-
  홍익열공이(HongikYeolgong2) 안드로이드 배포 전략 자동화 skill.
  "릴리즈 시작해줘"(start release) → 다음 버전을 먼저 물어보고 Release/x.y.z 브랜치 생성 + versionCode/versionName 자동 bump + 커밋·푸시.
  "배포해줘"(deploy) → 현재 Release 브랜치에서 main(자동 출시 트리거)과 develop(역병합) 두 곳으로 머지 PR을 자동으로 연다.
  릴리스 시작, 배포, 버전 올리기, 출시 관련 요청에 사용한다.
---

# 배포 전략 (Release / Deploy)

이 프로젝트의 gitflow와 자동 배포 파이프라인(`.github/workflows/release.yml`)에 맞춰 릴리스를 진행한다.

```
develop ──(릴리즈 시작)──▶ Release/x.y.z ──(배포: PR 2개)──┬──▶ main   → 머지 시 자동으로 Play Store(production) 출시 + 태그 x.y.z
                                                          └──▶ develop → 버전 bump 역병합
```

- 개발 기본 브랜치: `develop`
- 릴리스 브랜치: `Release/x.y.z` (대문자 R — `release.yml` 트리거가 `Release/`로 시작하는 head 브랜치만 인식)
- 배포 브랜치: `main` (여기로 머지되면 자동 배포)

## 버전 규칙 (반드시 지킬 것)

- `versionName` 은 시맨틱 버전 `MAJOR.MINOR.PATCH`.
- `versionCode` 는 **항상 `MAJOR*10000 + MINOR*100 + PATCH`** 로 계산한다. (전체 릴리스 히스토리에서 검증된 규칙)
  - 예) `1.5.0` → `10500`, `1.4.1` → `10401`, `2.0.0` → `20000`
- 두 값 모두 `gradle/libs.versions.toml` 상단의 `[versions]` 에 있다.
- Play Store는 versionCode가 직전보다 커야 하므로, 새 버전은 현재보다 반드시 커야 한다.

---

## 먼저 할 일: 어떤 단계인지 판별

사용자의 말과 현재 브랜치로 단계를 정한다.

- "릴리즈 시작 / 릴리스 시작 / 버전 올려 / 새 버전" → **Phase A (릴리즈 시작)**
- "배포 / 배포해줘 / 출시 / deploy" → **Phase B (배포)**
- 애매하면 현재 브랜치로 추정하고 확인한다: `Release/*` 위면 보통 배포(Phase B), `develop` 위면 보통 시작(Phase A).

---

## Phase A — 릴리즈 시작

### A-1. 사전 점검
1. `git fetch origin --tags` 로 최신화.
2. 작업 트리가 깨끗한지 `git status -s` 확인. 변경 사항이 있으면 사용자에게 알리고 중단(스태시/커밋 여부 확인).
3. 현재 버전 읽기:
   ```bash
   grep -E '^(versionCode|versionName)' gradle/libs.versions.toml
   ```

### A-2. 다음 버전 물어보기 (필수)
`AskUserQuestion` 으로 **다음 버전을 먼저 물어본다.** 현재 버전이 `X.Y.Z` 일 때 옵션과 각 옵션의 계산된 결과를 함께 보여준다:

- **Patch** `X.Y.(Z+1)` — 버그 수정 (예: 1.4.0 → 1.4.1, versionCode 10401)
- **Minor** `X.(Y+1).0` — 기능 추가 (예: 1.4.0 → 1.5.0, versionCode 10500)
- **Major** `(X+1).0.0` — 큰 변경 (예: 1.4.0 → 2.0.0, versionCode 20000)
- (사용자가 "Other"로 직접 버전을 입력할 수도 있음 — 입력값이 시맨틱 버전 형식인지 검증)

선택된 `versionName` 으로 `versionCode = MAJOR*10000 + MINOR*100 + PATCH` 를 계산한다.

### A-3. 검증
- 새 `versionName` 으로 된 태그가 이미 있으면 중단: `git rev-parse -q --verify "refs/tags/<버전>"` 가 성공하면 "이미 존재하는 버전"이라고 알리고 멈춘다.
- 새 `versionCode` 가 현재 값보다 큰지 확인.

### A-4. Release 브랜치 생성 + 버전 bump
1. 최신 develop에서 브랜치 생성:
   ```bash
   git switch -c Release/<버전> origin/develop
   ```
   (이미 `Release/<버전>` 이 있으면 사용자에게 알리고 그 브랜치로 switch 할지 확인)
2. `gradle/libs.versions.toml` 의 `versionCode`, `versionName` 을 Edit 도구로 새 값으로 교체.
3. 커밋 (이 저장소 커밋 스타일에 맞춤):
   ```bash
   git commit -am "🔖 Release <버전> 버전 준비 (versionCode <코드>)"
   ```
   커밋 메시지 마지막 줄에 다음을 포함:
   `Co-Authored-By: Claude Opus 4.8 (1M context) <noreply@anthropic.com>`
4. 푸시:
   ```bash
   git push -u origin Release/<버전>
   ```

### A-5. 결과 안내
- 생성된 `Release/<버전>` 브랜치와 bump된 버전을 알린다.
- 다음 단계 안내: QA/막바지 수정을 이 브랜치에서 진행한 뒤, **"배포해줘"** 라고 하면 PR을 연다고 알린다.

---

## Phase B — 배포

### B-1. 사전 점검
1. 현재 브랜치가 `Release/*` 인지 확인:
   ```bash
   git branch --show-current
   ```
   아니면: 어떤 Release 브랜치를 배포할지 사용자에게 묻고 switch.
2. 로컬 커밋이 origin에 푸시돼 있는지 확인. 안 돼 있으면 `git push` 먼저.
3. 버전 읽기: `grep -E '^(versionCode|versionName)' gradle/libs.versions.toml`.
4. 같은 base(main/develop)로 이미 열린 PR이 있는지 확인하여 중복 생성 방지:
   ```bash
   gh pr list --head <Release 브랜치> --state open
   ```

### B-2. 머지 PR 2개 열기 (자동 머지는 하지 않음 — 열기만 한다)
1. **main 으로 (배포 트리거):**
   ```bash
   gh pr create --base main --head <Release 브랜치> \
     --title "🚀 Release <버전>" \
     --body "<버전>(versionCode <코드>) 배포. 이 PR을 main에 머지하면 release.yml이 서명 AAB 빌드 → Play Store(production) 업로드 → main에 <버전> 태그 생성을 자동 수행합니다."
   ```
2. **develop 으로 (버전 bump 역병합):**
   ```bash
   gh pr create --base develop --head <Release 브랜치> \
     --title "🔀 Release <버전> develop 반영" \
     --body "<버전> 버전 bump 및 릴리스 브랜치 변경 사항을 develop에 역병합합니다."
   ```
   각 body 마지막 줄에 `🤖 Generated with [Claude Code](https://claude.com/claude-code)` 포함.

### B-3. 결과 안내
- 생성된 두 PR 링크를 보여준다.
- **중요 안내:** main PR을 머지하면 즉시 production 100% 출시되며 되돌리기 어렵다. develop PR은 버전 동기화용. 머지는 사용자가 직접 한다.
- 첫 배포라면 안전 검증을 위해 `release.yml` 의 `track: production` 을 임시로 `internal` 로 바꿔 한 번 돌려보는 것을 권할 수 있다.

---

## 주의사항
- 비밀번호/키스토어 등 시크릿을 커밋하거나 출력하지 않는다.
- PR을 **열기만** 하고 자동으로 머지하지 않는다 (특히 main → production 출시는 사람이 확인 후 머지).
- 전체 셋업(필요한 GitHub Secrets, Play 서비스 계정)은 `docs/RELEASE.md` 참고.
