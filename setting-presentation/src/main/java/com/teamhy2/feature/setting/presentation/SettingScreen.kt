package com.teamhy2.feature.setting.presentation

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamhy2.designsystem.common.HY2Dialog
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.feature.setting.domain.repository.model.UserInfo
import com.teamhy2.feature.setting.presentation.components.SettingButton
import com.teamhy2.feature.setting.presentation.components.SettingButtonWithSwitch
import com.teamhy2.feature.setting.presentation.components.SettingUserProfile
import com.teamhy2.feature.setting.presentation.model.SettingUiState
import com.teamhy2.hongikyeolgong2.setting.presentation.R

@Composable
fun SettingRoute(
    noticeUrl: String,
    onInquiryClick: () -> Unit,
    onLogoutOrWithdrawComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val settingUiState by viewModel.settingUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SettingScreen(
        settingUiState = settingUiState,
        onLogoutClick = viewModel::logout,
        onWithdrawClick = viewModel::withdraw,
        onNotificationSwitchClick = { isChecked ->
            viewModel.updateNotificationSwitchState(isChecked)
        },
        onNoticeClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(noticeUrl))
            context.startActivity(intent)
        },
        onInquiryClick = onInquiryClick,
        onLogoutOrWithdrawComplete = onLogoutOrWithdrawComplete,
        modifier = modifier,
    )
}

@Composable
fun SettingScreen(
    settingUiState: SettingUiState,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onNotificationSwitchClick: (Boolean) -> Unit,
    onNoticeClick: () -> Unit,
    onInquiryClick: () -> Unit,
    onLogoutOrWithdrawComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showWithdrawDialog by remember { mutableStateOf(false) }

    Log.d("bandal", "SettingScreen: $settingUiState")

    when (settingUiState) {
        is SettingUiState.Loading -> {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                androidx.compose.material3.CircularProgressIndicator()
            }
        }

        is SettingUiState.Success -> {
            if (settingUiState.isSignedOutOrWithDraw) {
                onLogoutOrWithdrawComplete()
            }
            if (showLogoutDialog) {
                HY2Dialog(
                    description = stringResource(R.string.setting_logout_dialog_description),
                    leftButtonText = stringResource(R.string.setting_logout_dialog_left_button_text),
                    rightButtonText = stringResource(R.string.setting_logout_dialog_right_button_text),
                    onLeftButtonClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    onRightButtonClick = {
                        showLogoutDialog = false
                    },
                    onDismiss = { showLogoutDialog = false },
                )
            }

            if (showWithdrawDialog) {
                HY2Dialog(
                    description = stringResource(R.string.setting_withdrawal_dialog_description),
                    leftButtonText = stringResource(R.string.setting_withdrawal_dialog_left_button_text),
                    rightButtonText = stringResource(R.string.setting_withdrawal_dialog_right_button_text),
                    onLeftButtonClick = {
                        showWithdrawDialog = false
                        onWithdrawClick()
                    },
                    onRightButtonClick = {
                        showWithdrawDialog = false
                    },
                    onDismiss = { showWithdrawDialog = false },
                )
            }

            Column(
                modifier = modifier.fillMaxSize(),
            ) {
                SettingBody(
                    userInfo = settingUiState.userInfo,
                    isNotificationSwitchChecked = settingUiState.isNotificationSwitchChecked,
                    onNotificationSwitchClick = onNotificationSwitchClick,
                    onNoticeClick = onNoticeClick,
                    onInquiryClick = onInquiryClick,
                )

                Spacer(modifier = Modifier.weight(1f))

                SettingBottom(
                    showLogoutDialog = { showLogoutDialog = true },
                    showWithdrawDialog = { showWithdrawDialog = true },
                )
            }
        }

        is SettingUiState.Error -> Unit
    }
}

@Composable
fun SettingBody(
    userInfo: UserInfo,
    isNotificationSwitchChecked: Boolean,
    onNotificationSwitchClick: (Boolean) -> Unit,
    onNoticeClick: () -> Unit,
    onInquiryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(start = 24.dp, end = 24.dp, top = 34.dp, bottom = 36.dp)
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingUserProfile(userInfo)
        Spacer(modifier = Modifier.height(20.dp))
        SettingButton(
            text = stringResource(R.string.setting_notice),
            onClick = onNoticeClick,
        )
        Spacer(modifier = Modifier.height(20.dp))
        SettingButton(
            text = stringResource(R.string.setting_inquiry),
            onClick = onInquiryClick,
        )
        Spacer(modifier = Modifier.height(20.dp))
        SettingButtonWithSwitch(
            text = stringResource(R.string.setting_notification_reminder),
            isChecked = isNotificationSwitchChecked,
            onCheckedChanged = onNotificationSwitchClick,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_information),
                contentDescription = null,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.setting_notification_reminder_description),
                color = Gray200,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }
    }
}

@Composable
fun SettingBottom(
    showLogoutDialog: () -> Unit,
    showWithdrawDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.setting_logout),
            color = Gray300,
            style = HY2Theme.typography.body05,
            modifier =
                Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) { showLogoutDialog() },
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = "|",
            color = Gray300,
            style = HY2Theme.typography.body05,
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = stringResource(R.string.setting_withdrawal),
            color = Gray300,
            style = HY2Theme.typography.body05,
            modifier =
                Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                ) { showWithdrawDialog() },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    val sampleUserInfo =
        UserInfo(
            name = "서재원",
            department = "전자전기공학부",
        )

    val state by remember {
        mutableStateOf(
            SettingUiState.Success(
                isNotificationSwitchChecked = true,
                isSignedOutOrWithDraw = false,
                userInfo = sampleUserInfo,
            ),
        )
    }

    HY2Theme {
        SettingScreen(
            settingUiState = state,
            onLogoutClick = {},
            onWithdrawClick = {},
            onNotificationSwitchClick = {},
            onNoticeClick = {},
            onInquiryClick = {},
            onLogoutOrWithdrawComplete = {},
            modifier = Modifier,
        )
    }
}
