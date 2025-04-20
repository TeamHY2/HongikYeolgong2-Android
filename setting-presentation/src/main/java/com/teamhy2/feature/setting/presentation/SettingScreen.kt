package com.teamhy2.feature.setting.presentation

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.teamhy2.designsystem.common.HY2CircularLoading
import com.teamhy2.designsystem.common.HY2Dialog
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.util.compositionlocal.LocalNavController
import com.teamhy2.designsystem.util.compositionlocal.LocalShowSnackBar
import com.teamhy2.designsystem.util.compositionlocal.LocalTracker
import com.teamhy2.feature.setting.presentation.components.SettingButton
import com.teamhy2.feature.setting.presentation.components.SettingButtonWithSwitch
import com.teamhy2.feature.setting.presentation.components.SettingUserProfile
import com.teamhy2.feature.setting.presentation.navigation.navigateToProfileModification
import com.teamhy2.hongikyeolgong2.setting.presentation.R
import com.teamhy2.user.domain.model.UserInfo
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingRoute(
    noticeUrl: String,
    onNavigateToInquiry: () -> Unit,
    onLogoutOrWithdrawComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val state: SettingState by viewModel.collectAsState()
    val context = LocalContext.current
    val tracker = LocalTracker.current

    val localShowSnackBar = LocalShowSnackBar.current
    val localNavController = LocalNavController.current

    viewModel.collectSideEffect {
        when (it) {
            is SettingSideEffect.ShowError -> {
                localShowSnackBar.showSnackBar(it.errorMessage)
            }

            is SettingSideEffect.LogoutOrWithdrawComplete -> {
                onLogoutOrWithdrawComplete()
            }

            SettingSideEffect.Navigation.ProfileModification -> {
                localNavController.navigateToProfileModification()
            }

            SettingSideEffect.Navigation.Inquiry -> {
                onNavigateToInquiry()
            }

            SettingSideEffect.Navigation.Notice -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(noticeUrl))
                context.startActivity(intent)
            }
        }
    }

    LaunchedEffect(true) {
        tracker.trackEvent("Setting")
        viewModel.initSettingScreen()
    }

    SettingScreen(
        modifier = modifier,
        state = state,
        onClick = {
            logout = {
                viewModel.logout()
                tracker.trackEvent("LogoutButton")
            }
            withdraw = {
                viewModel.withdraw()
                tracker.trackEvent("WithdrawButton")
            }
            notificationSwitch = { isChecked ->
                viewModel.toggleNotificationSwitch(isChecked)
            }
            notice = {
                viewModel.onNoticeButtonClick()
            }
            inquiry = {
                viewModel.onInquiryButtonClick()
            }
            profileModify = {
                viewModel.onProfileModifyClick()
            }
        },
    )
}

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    state: SettingState,
    onClick: OnClick.() -> Unit = {},
) {
    val onItemClick = remember(onClick) { OnClick().apply(onClick) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var showWithdrawDialog by remember { mutableStateOf(false) }

    if (showSignOutDialog) {
        HY2Dialog(
            description = stringResource(R.string.setting_logout_dialog_description),
            leftButtonText = stringResource(R.string.setting_logout_dialog_left_button_text),
            rightButtonText = stringResource(R.string.setting_logout_dialog_right_button_text),
            onLeftButtonClick = {
                showSignOutDialog = false
                onItemClick.logout()
            },
            onRightButtonClick = {
                showSignOutDialog = false
            },
            onDismiss = { showSignOutDialog = false },
        )
    }

    if (showWithdrawDialog) {
        HY2Dialog(
            description = stringResource(R.string.setting_withdrawal_dialog_description),
            leftButtonText = stringResource(R.string.setting_withdrawal_dialog_left_button_text),
            rightButtonText = stringResource(R.string.setting_withdrawal_dialog_right_button_text),
            onLeftButtonClick = {
                showWithdrawDialog = false
                onItemClick.withdraw()
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
            userInfo = state.userInfo,
            isNotificationSwitchChecked = state.isNotificationSwitchChecked,
            onNotificationSwitchClick = onItemClick.notificationSwitch,
            onNoticeClick = onItemClick.notice,
            onInquiryClick = onItemClick.inquiry,
            onProfileClick = onItemClick.profileModify,
        )

        Spacer(modifier = Modifier.weight(1f))

        SettingBottom(
            showLogoutDialog = { showSignOutDialog = true },
            showWithdrawDialog = { showWithdrawDialog = true },
        )
    }

    if (state.isLoading) {
        HY2CircularLoading()
    }
}

@Composable
fun SettingBody(
    userInfo: UserInfo,
    isNotificationSwitchChecked: Boolean,
    onNotificationSwitchClick: (Boolean) -> Unit,
    onNoticeClick: () -> Unit,
    onInquiryClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(start = 24.dp, end = 24.dp, top = 34.dp)
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SettingUserProfile(
            userInfo = userInfo,
            onProfileClick = onProfileClick,
        )
        Spacer(modifier = Modifier.height(24.dp))
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
                color = Gray300,
                style = HY2Typography().caption,
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
                .padding(bottom = 40.dp),
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

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun SettingScreenPreview() {
    val sampleUserInfo =
        UserInfo(
            nickname = "서재원",
            email = "librarywon@gmail.com",
            department = "전자전기공학부",
        )

    val state by remember {
        mutableStateOf(
            SettingState(
                isNotificationSwitchChecked = true,
                userInfo = sampleUserInfo,
            ),
        )
    }

    HY2Theme {
        SettingScreen(
            state = state,
            onClick = {},
            modifier = Modifier,
        )
    }
}
