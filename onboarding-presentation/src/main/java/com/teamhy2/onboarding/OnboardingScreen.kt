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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.common.SignInButton
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.onboarding.presentation.R

@Composable
fun OnboardingRoute(
    onGoogleLoginClick: () -> Unit,
    onGoogleLoginDone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingScreen(
        onGoogleLoginClick = {
            onGoogleLoginClick()
            onGoogleLoginDone()
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onGoogleLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val onboardingImages =
        listOf(
            R.drawable.img_onboarding1,
            R.drawable.img_onboarding2,
            R.drawable.img_onboarding3,
        )

    val pagerState = rememberPagerState(pageCount = { onboardingImages.size })

    Column(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier =
                Modifier
                    .padding(top = 55.dp)
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
            factory = { context ->
                val signInButton = SignInButton(context)
                signInButton.setOnClickListener {
                    onGoogleLoginClick()
                }
                signInButton
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
        )
    }
}

private const val DOT_INDICATOR_ACTIVE_SIZE = 16
private const val DOT_INDICATOR_INACTIVE_SIZE = 9
private const val DOT_INDICATOR_SPACING = 6

@Composable
fun DotsIndicator(
    count: Int,
    index: Int,
    inactiveColor: Color = Gray600,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(count) { dotIndex ->
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .padding(DOT_INDICATOR_SPACING.dp)
                        .size(16.dp),
            ) {
                when (index == dotIndex) {
                    true -> {
                        Image(
                            painter = painterResource(id = R.drawable.ic_onboarding_star),
                            contentDescription = null,
                            modifier =
                                Modifier
                                    .size(DOT_INDICATOR_ACTIVE_SIZE.dp),
                        )
                    }

                    false -> {
                        Box(
                            modifier =
                                Modifier
                                    .size(DOT_INDICATOR_INACTIVE_SIZE.dp)
                                    .clip(CircleShape)
                                    .background(inactiveColor),
                        )
                    }
                }
            }
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
