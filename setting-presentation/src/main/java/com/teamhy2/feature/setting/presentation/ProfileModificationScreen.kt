package com.teamhy2.feature.setting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.teamhy2.designsystem.common.HY2Dialog
import com.teamhy2.designsystem.common.HY2DropdownTextField
import com.teamhy2.designsystem.common.HY2TextField
import com.teamhy2.designsystem.common.ThrottleButton
import com.teamhy2.designsystem.ui.theme.BackgroundBlack
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Blue400
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray400
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.designsystem.ui.theme.Yellow300
import com.teamhy2.designsystem.util.compositionlocal.LocalNavController
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.ShowSnackBar
import com.teamhy2.designsystem.util.modifier.addFocusCleaner
import com.teamhy2.designsystem.util.modifier.throttleClickable
import com.teamhy2.onboarding.NicknameState
import com.teamhy2.onboarding.presentation.R
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileModificationRoute(profileModificationViewModel: ProfileModificationViewModel = hiltViewModel()) {
    val localShowSnackBar: ShowSnackBar = LocalShowSnackBar.current
    val localNavController: NavController = LocalNavController.current
    val state by profileModificationViewModel.collectAsState()

    profileModificationViewModel.collectSideEffect {
        when (it) {
            is ProfileModificationSideEffect.ShowError -> localShowSnackBar.showSnackBar(it.throwable.message)
            is ProfileModificationSideEffect.ProfileModificationFailure -> {
                localShowSnackBar.showSnackBar("프로필 수정에 실패하였습니다.")
            }

            is ProfileModificationSideEffect.ProfileModificationSucceed -> {
                localNavController.popBackStack()
            }
        }
    }

    ProfileModificationScreen(
        nickname = state.userInfo.nickname,
        isNicknameValidate = state.isNicknameValidate,
        nicknameState = state.nicknameState,
        isDepartmentValidate = state.isDepartmentValidate,
        department = state.userInfo.department,
        departments = state.departments,
        onDepartmentChange = profileModificationViewModel::updateDepartment,
        onNicknameChange = profileModificationViewModel::updateNickname,
        onNicknameDuplicateCheckClicked = profileModificationViewModel::checkNicknameDuplication,
        onModificationButtonClicked = profileModificationViewModel::updateUserInfo,
        onBackClick = { localNavController.popBackStack() },
    )
}

@Composable
fun ProfileModificationScreen(
    nickname: String,
    isNicknameValidate: Boolean,
    nicknameState: NicknameState,
    isDepartmentValidate: Boolean,
    department: String,
    departments: List<String>,
    onDepartmentChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onNicknameDuplicateCheckClicked: () -> Unit,
    onModificationButtonClicked: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    val focusManager = LocalFocusManager.current

    var isAskAgainDialogOpen by remember { mutableStateOf(false) }

    if (isAskAgainDialogOpen) {
        HY2Dialog(
            description = "프로필 변경을 진행하실건가요?",
            leftButtonText = "돌아가기",
            rightButtonText = "변경하기",
            onLeftButtonClick = { isAskAgainDialogOpen = false },
            onRightButtonClick = {
                onModificationButtonClicked()
                isAskAgainDialogOpen = false
            },
            onDismiss = { isAskAgainDialogOpen = false },
        )
    }

    Column(
        modifier =
            modifier
                .background(BackgroundBlack)
                .addFocusCleaner(focusManager)
                .padding(horizontal = 32.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .offset(x = (-8).dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier.size(40.dp)
                        .clip(CircleShape)
                        .throttleClickable(onClick = onBackClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = com.teamhy2.hongikyeolgong2.setting.presentation.R.drawable.ic_back),
                    contentDescription = null,
                    tint = Gray100,
                )
            }
            Text(
                text = "프로필 변경",
                style = HY2Typography().head,
                color = Gray100,
            )
        }
        Spacer(modifier = Modifier.height(22.dp))
        Text(
            text = stringResource(R.string.sign_up_nickname_title),
            style = HY2Typography().title03,
            color = Gray200,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            HY2TextField(
                value = nickname,
                onValueChange = onNicknameChange,
                modifier = Modifier.weight(1f),
                focusRequester = focusRequester,
                hintText = stringResource(R.string.sign_up_nickname_hint),
                isInvalid = isNicknameValidate.not() || nicknameState == NicknameState.DUPLICATED,
            )
            Spacer(modifier = Modifier.width(12.dp))
            ThrottleButton(
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Blue100,
                        disabledContainerColor = Blue400,
                    ),
                shape = RoundedCornerShape(8.dp),
                onClick = onNicknameDuplicateCheckClicked,
                enabled = isNicknameValidate && nicknameState == NicknameState.NOT_CHECKED,
                modifier = Modifier.height(48.dp),
            ) {
                Text(
                    text = stringResource(R.string.sign_up_duplication_check),
                    style = HY2Typography().body05,
                    color =
                        if (nicknameState == NicknameState.DUPLICATED ||
                            nicknameState == NicknameState.NOT_DUPLICATED ||
                            isNicknameValidate.not()
                        ) {
                            White.copy(
                                alpha = 0.4f,
                            )
                        } else {
                            White
                        },
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text =
                when {
                    nickname.isEmpty() -> stringResource(id = R.string.sign_up_nickname_hint_text)
                    nicknameState == NicknameState.DUPLICATED -> stringResource(id = R.string.sign_up_nickname_duplicated)
                    isNicknameValidate && nicknameState == NicknameState.NOT_CHECKED ->
                        stringResource(
                            id = R.string.sign_up_nickname_hint_text,
                        )

                    isNicknameValidate -> stringResource(id = R.string.sign_up_nickname_can_use_text)
                    else -> stringResource(R.string.sign_up_nickname_error_text)
                },
            style = HY2Typography().caption,
            color =
                if (nickname.isBlank()) {
                    Gray400
                } else if (isNicknameValidate.not() || nicknameState == NicknameState.DUPLICATED) {
                    Yellow300
                } else {
                    Blue100
                },
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.sign_up_department_title),
            style = HY2Typography().title03,
            color = Gray200,
        )
        Spacer(modifier = Modifier.height(8.dp))
        HY2DropdownTextField(
            options = departments,
            hintText = stringResource(R.string.sign_up_department_hint),
            value = department,
            onValueChanged = onDepartmentChange,
            focusManager = focusManager,
            focusRequester = focusRequester,
        )

        Spacer(modifier = Modifier.weight(1f))

        HY2GradientMainButton(
            onClick = { isAskAgainDialogOpen = true },
            text = "변경하기",
            enabled = nicknameState == NicknameState.NOT_DUPLICATED && isNicknameValidate && isDepartmentValidate,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun HY2GradientMainButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier =
            modifier
                .fillMaxWidth()
                .paint(
                    painter =
                        painterResource(
                            id = if (enabled) R.drawable.img_gradient_main_button_enabled else R.drawable.img_gradient_main_button_disabled,
                        ),
                    contentScale = ContentScale.Fit,
                )
                .throttleClickable(
                    enabled = enabled,
                    onClick = onClick,
                ),
    ) {
        Text(
            text = text,
            style = HY2Typography().title02,
            color = if (enabled) Gray100 else Gray100.copy(alpha = 0.5f),
        )
    }
}

@Preview
@Composable
private fun ProfileModificationScreenPreview() {
    var nickname by remember { mutableStateOf("") }

    HY2Theme {
        ProfileModificationScreen(
            nickname = nickname,
            isNicknameValidate = false,
            nicknameState = NicknameState.NOT_CHECKED,
            isDepartmentValidate = false,
            onNicknameChange = { nickname = it },
            onNicknameDuplicateCheckClicked = {},
            departments = emptyList(),
            department = "",
            onModificationButtonClicked = {},
            onDepartmentChange = {},
            onBackClick = {},
        )
    }
}
