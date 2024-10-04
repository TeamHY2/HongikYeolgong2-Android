package com.teamhy2.feature.setting.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.feature.setting.presentation.model.UserInfo
import com.teamhy2.hongikyeolgong2.setting.presentation.R.drawable.img_settting_profile

private const val PROFILE_IMAGE_SIZE = 56

@Composable
fun SettingUserProfile(
    userInfo: UserInfo,
    profileImage: Painter = painterResource(id = img_settting_profile),
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.width(1.dp))
        Image(
            painter = profileImage,
            contentDescription = null,
            modifier =
                Modifier
                    .size(PROFILE_IMAGE_SIZE.dp),
        )
        Spacer(modifier = Modifier.width(20.dp))
        Row {
            Text(
                text = userInfo.name,
                style = HY2Typography().body05,
                color = Gray200,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "|",
                style = HY2Typography().body05,
                color = Gray200,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = userInfo.department,
                style = HY2Typography().body05,
                color = Gray200,
            )
        }
    }
}

@Preview
@Composable
private fun SettingUserProfilePreview() {
    val userInfo =
        UserInfo(
            name = "유림",
            department = "디자인컨버전스학부",
        )
    SettingUserProfile(userInfo)
}
