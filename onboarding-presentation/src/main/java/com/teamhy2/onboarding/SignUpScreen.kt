package com.teamhy2.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.common.HY2DropdownTextField
import com.teamhy2.designsystem.common.HY2TextField
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Blue400
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.onboarding.presentation.R

@Composable
fun SignUpRoute(
    onSignUpButtonClicked: () -> Unit,
    alreadySignedUp: () -> Unit,
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by signUpViewModel.signUpUiState.collectAsStateWithLifecycle()
    val nickname by signUpViewModel.nickname.collectAsStateWithLifecycle()
    val department by signUpViewModel.department.collectAsStateWithLifecycle()
    val isUserAlreadySignedUp by signUpViewModel.isUserAlreadySignedUp.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (isUserAlreadySignedUp) {
            alreadySignedUp()
        }
    }

    SignUpScreen(
        nickname = nickname,
        isNicknameValidate = uiState.isNicknameValidate,
        isNicknameNotDuplicated = uiState.isNicknameNotDuplicated,
        isDepartmentValidate = uiState.isDepartmentValidate,
        department = department,
        departments = uiState.departments,
        onDepartmentChange = signUpViewModel::updateDepartment,
        onNicknameChange = signUpViewModel::updateNickname,
        onNicknameDuplicateCheckClicked = signUpViewModel::checkNicknameDuplication,
        onSignUpButtonClicked = {
            signUpViewModel.signUp()
            onSignUpButtonClicked()
        },
        modifier = modifier,
    )
}

@Composable
fun SignUpScreen(
    nickname: String,
    isNicknameValidate: Boolean,
    isNicknameNotDuplicated: Boolean,
    isDepartmentValidate: Boolean,
    department: String,
    departments: List<String>,
    onDepartmentChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onNicknameDuplicateCheckClicked: () -> Unit,
    onSignUpButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(horizontal = 32.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(52.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = stringResource(R.string.sign_up_title),
                style = HY2Typography().head,
                color = Gray100,
            )
        }
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
                hintText = stringResource(R.string.sign_up_nickname_hint),
                modifier = Modifier.weight(1f),
                isInvalid = isNicknameValidate.not(),
                supportingText = if (isNicknameValidate) "" else stringResource(R.string.sign_up_nickname_supporting_text),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Blue100,
                        disabledContainerColor = Blue400,
                    ),
                shape = RoundedCornerShape(8.dp),
                onClick = onNicknameDuplicateCheckClicked,
                enabled = isNicknameValidate && isNicknameNotDuplicated.not(),
                modifier = Modifier.height(48.dp),
            ) {
                Text(
                    text = stringResource(R.string.sign_up_duplication_check),
                    style = HY2Typography().body05,
                    color = if (isNicknameValidate) White else White.copy(alpha = 0.4f),
                )
            }
        }
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
        )

        Spacer(modifier = Modifier.weight(1f))

        HY2GradientMainButton(
            onClick = onSignUpButtonClicked,
            text = stringResource(R.string.sign_up_sign_up_button_text),
            enabled = isNicknameNotDuplicated && isNicknameValidate && isDepartmentValidate,
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
                .clickable(
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
private fun SignUpScreenPreview() {
    var nickname by remember { mutableStateOf("") }

    HY2Theme {
        SignUpScreen(
            nickname = nickname,
            isNicknameValidate = false,
            isNicknameNotDuplicated = true,
            isDepartmentValidate = false,
            onNicknameChange = { nickname = it },
            onNicknameDuplicateCheckClicked = {},
            departments = emptyList(),
            department = "",
            onSignUpButtonClicked = {},
            onDepartmentChange = {},
        )
    }
}
