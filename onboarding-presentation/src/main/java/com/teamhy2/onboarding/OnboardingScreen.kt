package com.teamhy2.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton
import com.teamhy2.designsystem.ui.theme.Black
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.Yellow100
import com.teamhy2.onboarding.presentation.R

@Composable
fun OnboardingRoute(
    modifier: Modifier = Modifier,
    onGoogleLoginClick: () -> Unit,
) {
    OnboardingScreen(onGoogleLoginClick = onGoogleLoginClick)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onGoogleLoginClick: () -> Unit,
) {
    val onboardingImages =
        listOf(
            R.drawable.img_test_1,
            R.drawable.img_test_2,
            R.drawable.img_test_3,
            R.drawable.img_test_4,
        )

    val pagerState = rememberPagerState(pageCount = { onboardingImages.size })

    Column(
        modifier =
            modifier
                .background(Black)
                .fillMaxSize(),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
        ) { page ->
            Image(
                painter = painterResource(id = onboardingImages[page]),
                contentDescription =
                    stringResource(
                        R.string.onboarding_app_description_image,
                        page,
                    ),
                modifier = Modifier.fillMaxSize(),
            )
        }
        DotsIndicator(
            modifier =
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            count = pagerState.pageCount,
            index = pagerState.currentPage,
        )
        AndroidView(
            factory = {
                SignInButton(it).apply {
                    setOnClickListener {
                        onGoogleLoginClick()
                    }
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
        )
    }
}

private const val DOT_INDICATOR_SIZE = 8
private const val DOT_INDICATOR_SPACING = 4

@Composable
fun DotsIndicator(
    count: Int,
    index: Int,
    activeColor: Color = Yellow100,
    inactiveColor: Color = Gray300,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(count) { iteration ->
            val color = if (index == iteration) activeColor else inactiveColor
            Box(
                modifier =
                    Modifier
                        .padding(DOT_INDICATOR_SPACING.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(DOT_INDICATOR_SIZE.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    HY2Theme {
        OnboardingScreen(
            modifier = Modifier.fillMaxSize(),
            onGoogleLoginClick = { },
        )
    }
}
