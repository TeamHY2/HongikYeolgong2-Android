#!/bin/bash

# 1. 인자 확인 (이름과 경로)
BASE_NAME=$1
FULL_DIR_PATH=$2

if [ -z "$BASE_NAME" ] || [ -z "$FULL_DIR_PATH" ]; then
  echo "오류: 이름과 경로가 올바르게 전달되지 않았습니다."
  exit 1
fi

# 2. 패키지 이름 추출
PACKAGE_PATH=$(echo "$FULL_DIR_PATH" | sed 's|.*/java/||; s|.*/kotlin/||' | tr '/' '.')

# 3. 패키지 경로가 비어있는지 확인
if [ -z "$PACKAGE_PATH" ]; then
  echo "오류: 패키지 경로를 자동으로 계산할 수 없습니다."
  echo "선택하신 디렉토리의 전체 경로에 'java/' 또는 'kotlin/' 폴더가 포함되어 있는지 확인해주세요."
  exit 1
fi

# 4. 최종 파일 경로 변수 설정
VIEW_MODEL_FILE="$FULL_DIR_PATH/${BASE_NAME}ViewModel.kt"
CONTRACT_FILE="$FULL_DIR_PATH/${BASE_NAME}Contract.kt"
SCREEN_FILE="$FULL_DIR_PATH/${BASE_NAME}Screen.kt" # Screen 파일 경로 추가

# 5. Contract.kt 파일 생성
cat <<EOF > "$CONTRACT_FILE"
package $PACKAGE_PATH

import com.teamhy2.designsystem.util.mvi.SideEffect
import com.teamhy2.designsystem.util.mvi.UiIntent
import com.teamhy2.designsystem.util.mvi.UiState

sealed interface ${BASE_NAME}UiState : UiState {
    data object Loading : ${BASE_NAME}UiState
}

sealed interface ${BASE_NAME}UiIntent : UiIntent {
    // TODO: 사용자 이벤트에 따른 UiIntent 구현
}

sealed interface ${BASE_NAME}SideEffect : SideEffect {
    data class ShowSnackBar(val throwable: Throwable) : ${BASE_NAME}SideEffect
}
EOF

# 6. ViewModel.kt 파일 생성
cat <<EOF > "$VIEW_MODEL_FILE"
package $PACKAGE_PATH

import com.teamhy2.designsystem.util.mvi.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ${BASE_NAME}ViewModel
    @Inject
    constructor() : MviViewModel<${BASE_NAME}UiIntent, ${BASE_NAME}UiState, ${BASE_NAME}SideEffect>(${BASE_NAME}UiState.Loading) {
        override suspend fun reduceState(
            current: ${BASE_NAME}UiState,
            intent: ${BASE_NAME}UiIntent,
        ): ${BASE_NAME}UiState {
            return when (intent) {
                // TODO: UiIntent에 따른 상태 처리 구현
            }
        }
    }
EOF

# 7. Screen.kt 파일 생성
cat <<EOF > "$SCREEN_FILE"
package $PACKAGE_PATH

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ${BASE_NAME}Route(
    modifier: Modifier = Modifier,
    viewModel: ${BASE_NAME}ViewModel = hiltViewModel(),
) {
    val state: ${BASE_NAME}UiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is ${BASE_NAME}SideEffect.ShowSnackBar -> {
                    // TODO: SnackBar 표시 로직 구현
                }
            }
        }
    }

    ${BASE_NAME}Content(
        state = state,
        modifier = modifier,
    )
}

@Composable
fun ${BASE_NAME}Content(
    state: ${BASE_NAME}UiState,
    modifier: Modifier = Modifier,
) {
    when (state) {
        // TODO: UiState에 따른 컴포저블 구현
    }
}
EOF

echo "파일 3개 생성 완료! (경로: $FULL_DIR_PATH)"
echo "  - ${BASE_NAME}ViewModel.kt"
echo "  - ${BASE_NAME}Contract.kt"
echo "  - ${BASE_NAME}Screen.kt"
