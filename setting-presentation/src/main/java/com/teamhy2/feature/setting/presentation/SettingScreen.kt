package com.teamhy2.feature.setting.presentation

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
import androidx.compose.material3.IconButton
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
import com.teamhy2.feature.setting.presentation.components.SettingButton
import com.teamhy2.feature.setting.presentation.components.SettingButtonWithSwitch
import com.teamhy2.feature.setting.presentation.model.SettingUiState
import com.teamhy2.hongikyeolgong2.setting.presentation.R

@Composable
fun SettingRoute(
    onBackButtonClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onInquiryClick: () -> Unit,
    onLogoutOrWithdrawComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val settingUiState by viewModel.settingUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    SettingScreen(
        settingUiState = settingUiState,
        onLogoutClick = {
            viewModel.onLogoutClick(context)
            onLogoutOrWithdrawComplete()
        },
        onWithdrawClick = {
            viewModel.onWithdrawClick(context)
            onLogoutOrWithdrawComplete()
        },
        onNotificationSwitchClick = { isChecked ->
            viewModel.updateNotificationSwitchState(isChecked)
        },
        onBackButtonClick = onBackButtonClick,
        onNoticeClick = onNoticeClick,
        onInquiryClick = onInquiryClick,
        modifier = modifier,
    )
}

@Composable
fun SettingScreen(
    settingUiState: SettingUiState,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit,
    onNotificationSwitchClick: (Boolean) -> Unit,
    onBackButtonClick: () -> Unit,
    onNoticeClick: () -> Unit,
    onInquiryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showWithdrawDialog by remember { mutableStateOf(false) }

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
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        IconButton(
            onClick = { onBackButtonClick() },
            modifier = Modifier.padding(start = 20.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.setting_back_button_description),
            )
        }
        Column(
            modifier =
                Modifier
                    .padding(start = 32.dp, end = 32.dp, top = 14.dp, bottom = 16.dp)
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
                isChecked = settingUiState.isNotificationSwitchChecked,
                onCheckedChanged = { isChecked ->
                    onNotificationSwitchClick(isChecked)
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_information),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(14.dp)
                            .align(Alignment.CenterVertically),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.setting_notification_reminder_description),
                    color = Gray200,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
            }
            Spacer(modifier = Modifier.weight(1f))

            val interactionSource: MutableInteractionSource =
                remember { MutableInteractionSource() }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
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
                        ) { showLogoutDialog = true },
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
                        ) { showWithdrawDialog = true },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    val state by remember { mutableStateOf(SettingUiState(isNotificationSwitchChecked = true)) }

    HY2Theme {
        SettingScreen(
            settingUiState = state,
            onLogoutClick = {},
            onWithdrawClick = {},
            onNotificationSwitchClick = {},
            onBackButtonClick = {},
            onNoticeClick = {},
            onInquiryClick = {},
            modifier = Modifier,
        )
    }
}
