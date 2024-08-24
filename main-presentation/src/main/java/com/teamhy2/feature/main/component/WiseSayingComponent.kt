package com.teamhy2.feature.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.main.presentation.R

@Composable
fun WiseSayingComponent(
    quote: String,
    author: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_main_three_stars),
            contentDescription = null,
        )

        Text(
            text = quote,
            style = HY2Typography().body06,
            color = Gray100,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, bottom = 12.dp),
        )

        Text(
            text = "- $author",
            style = HY2Typography().caption,
            color = Gray200,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WiseSayingComponentPreview() {
    HY2Theme {
        WiseSayingComponent(
            quote = "행동보다 빠르게 불안감을\n 없앨 수 있는 것은 없습니다.",
            author = "월터 앤더슨",
            modifier = Modifier,
        )
    }
}
