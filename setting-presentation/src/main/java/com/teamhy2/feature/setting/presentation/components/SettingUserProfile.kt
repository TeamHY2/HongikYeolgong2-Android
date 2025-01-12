package com.teamhy2.feature.setting.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.util.modifier.throttleClickable
import com.teamhy2.hongikyeolgong2.setting.presentation.R
import com.teamhy2.hongikyeolgong2.setting.presentation.R.drawable.img_settting_profile
import com.teamhy2.user.domain.model.UserInfo

private const val PROFILE_IMAGE_SIZE = 56

@Composable
fun SettingUserProfile(
    userInfo: UserInfo,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    profileImageUrl: String = "",
) {
    val profileImagePainter =
        if (profileImageUrl.isNotBlank()) {
            rememberAsyncImagePainter(model = profileImageUrl)
        } else {
            painterResource(id = img_settting_profile)
        }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth().throttleClickable(onClick = onProfileClick),
    ) {
        Image(
            painter = profileImagePainter,
            contentDescription = null,
            modifier = Modifier.size(PROFILE_IMAGE_SIZE.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = userInfo.nickname,
                style = HY2Typography().title02,
                color = Gray100,
            )
            Icon(
                painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = Gray100,
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = userInfo.department,
            style = HY2Typography().body05,
            color = Gray100,
        )
    }
}

@Preview
@Composable
private fun SettingUserProfilePreview() {
    val userInfo =
        UserInfo(
            nickname = "유림",
            email = "urim@gmail.com",
            department = "디자인컨버전스학부",
        )
    SettingUserProfile(
        userInfo = userInfo,
        onProfileClick = {},
    )
}
