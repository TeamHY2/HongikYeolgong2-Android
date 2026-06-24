import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.common.HY2Spacer
import com.teamhy2.designsystem.ui.theme.Blue50
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.main.presentation.R

@Composable
fun StudyLamp(
    isOn: Boolean,
    username: String,
    studyTime: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Image(
            painter =
                painterResource(
                    R.drawable.ic_lamp_on.takeIf { isOn }
                        ?: R.drawable.ic_lamp_off,
                ),
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
        )
        HY2Spacer(4)
        Text(
            text = username,
            style = HY2Typography().body07,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            color = Blue50.takeIf { isOn } ?: Gray300,
        )
        Text(
            text = studyTime,
            style = HY2Typography().body03,
            color = Blue50.takeIf { isOn } ?: Gray300,
        )
    }
}

@Preview
@Composable
private fun StudyLampOnPreview() {
    HY2Theme {
        StudyLamp(
            isOn = true,
            username = "매우긴닉네임입니다",
            studyTime = "9:00:00",
            modifier = Modifier.width(80.dp),
        )
    }
}

@Preview
@Composable
private fun StudyLampOffPreview() {
    HY2Theme {
        StudyLamp(
            isOn = false,
            username = "반달",
            studyTime = "10:00:00",
            modifier = Modifier.width(80.dp),
        )
    }
}
