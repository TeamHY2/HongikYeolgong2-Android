package com.teamhy2.friend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.common.ActivatedToggleButton
import com.teamhy2.designsystem.common.HY2Button
import com.teamhy2.designsystem.common.HY2Spacer
import com.teamhy2.designsystem.common.HY2ToggleTextButton
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.friend.component.FriendItem
import com.teamhy2.friend.component.NotificationButton
import com.teamhy2.friend.domain.model.Friend
import kotlin.time.Duration.Companion.seconds

private val ScreenHorizontalPadding = 32.dp
private val ScreenVerticalPadding = 36.dp
private val FriendAddButtonHeight = 52.dp
private val FriendAddButtonBottomPadding = 16.dp

@Composable
internal fun FriendScreen(
    selectedRecordFilterType: RecordFilterType,
    isNotificationOn: Boolean,
    friends: List<Friend>,
    onAddFriendClick: () -> Unit,
    onNotificationButtonClick: () -> Unit,
    onRecordFilterClick: (RecordFilterType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val bottomDimAlpha by remember(listState) {
        derivedStateOf {
            calculateBottomDimAlpha(listState.layoutInfo)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = ScreenHorizontalPadding,
                        vertical = ScreenVerticalPadding,
                    ),
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                HY2ToggleTextButton(
                    activatedButton =
                        ActivatedToggleButton.LEFT.takeIf { selectedRecordFilterType == RecordFilterType.MONTHLY }
                            ?: ActivatedToggleButton.RIGHT,
                    leftText = "월간",
                    rightText = "일간",
                    onClick = { onRecordFilterClick(selectedRecordFilterType.toggled()) },
                )
                NotificationButton(
                    isNotificationOn = isNotificationOn,
                    onClick = onNotificationButtonClick,
                )
            }
            HY2Spacer(20)
            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding =
                    PaddingValues(
                        bottom = FriendAddButtonHeight + FriendAddButtonBottomPadding,
                    ),
            ) {
                itemsIndexed(
                    items = friends,
                    key = { _, friend -> friend.id },
                ) { index, friend ->
                    FriendItem(rankNumber = index + 1, friend = friend)
                }
            }
        }
        if (bottomDimAlpha > 0f) {
            val dimmedColor = Gray800.copy(alpha = 0.85f * bottomDimAlpha)
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .background(
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Gray800.copy(alpha = 0f),
                                        dimmedColor,
                                    ),
                            ),
                        ),
            )
        }
        HY2Button(
            text = "친구 추가하기",
            onClick = onAddFriendClick,
            textColor = Gray100,
            backgroundColor = Gray600,
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = ScreenHorizontalPadding,
                        end = ScreenHorizontalPadding,
                        bottom = ScreenVerticalPadding,
                    )
                    .height(FriendAddButtonHeight)
                    .fillMaxWidth(),
        )
    }
}

private fun calculateBottomDimAlpha(layoutInfo: LazyListLayoutInfo): Float {
    val totalItemsCount: Int = layoutInfo.totalItemsCount
    if (totalItemsCount == 0) return 0f

    val lastVisibleItem: LazyListItemInfo = layoutInfo.visibleItemsInfo.lastOrNull() ?: return 0f
    val maxIndex = totalItemsCount - 1
    val viewportEnd: Int = layoutInfo.viewportEndOffset

    val isLastItemFullyVisible =
        lastVisibleItem.index == maxIndex && lastVisibleItem.offset + lastVisibleItem.size <= viewportEnd
    if (isLastItemFullyVisible) return 0f

    val remainingItems: Int = (maxIndex - lastVisibleItem.index).coerceAtLeast(0)
    val fadeThresholdItems = 3f
    val partialRemaining =
        if (lastVisibleItem.index == maxIndex) {
            val visibleHeight =
                (viewportEnd - lastVisibleItem.offset).coerceIn(0, lastVisibleItem.size)
            val visibleFraction =
                if (lastVisibleItem.size == 0) {
                    1f
                } else {
                    visibleHeight / lastVisibleItem.size.toFloat()
                }
            (1f - visibleFraction).coerceIn(0f, 1f)
        } else {
            1f
        }

    val effectiveRemaining = remainingItems.toFloat() + partialRemaining
    return (effectiveRemaining / fadeThresholdItems).coerceIn(0f, 1f)
}

private class FriendScreenPreviewParameterProvider : PreviewParameterProvider<List<Friend>> {
    override val values =
        sequenceOf(
            (1..20).map { index ->
                Friend(
                    id = index.toLong(),
                    userId = index.toLong(),
                    nickname = "친구$index",
                    studyTime = (3600 * (index % 10) + 60 * (index % 60)).seconds,
                )
            },
            listOf(
                Friend(
                    id = 1,
                    userId = 1,
                    nickname = "짧은 이름",
                    studyTime = 30.seconds,
                ),
                Friend(
                    id = 2,
                    userId = 2,
                    nickname = "중간 이름의 친구",
                    studyTime = (3600 * 5 + 300).seconds,
                ),
                Friend(
                    id = 3,
                    userId = 3,
                    nickname = "매우 긴 이름을 가진 친구",
                    studyTime = (3600 * 24 + 1800).seconds,
                ),
            ),
            listOf(
                Friend(id = 1, userId = 1, nickname = "0시간 친구", studyTime = 0.seconds),
                Friend(id = 2, userId = 2, nickname = "1분 친구", studyTime = 60.seconds),
                Friend(id = 3, userId = 3, nickname = "1시간 친구", studyTime = 3600.seconds),
                Friend(id = 4, userId = 4, nickname = "10시간 친구", studyTime = 36000.seconds),
                Friend(id = 5, userId = 5, nickname = "100시간 친구", studyTime = 360000.seconds),
            ),
        )
}

@Preview
@Composable
private fun FriendScreenPreview(
    @PreviewParameter(FriendScreenPreviewParameterProvider::class) friends: List<Friend>,
) {
    HY2Theme {
        FriendScreen(
            isNotificationOn = true,
            friends = friends,
            onAddFriendClick = {},
            onRecordFilterClick = {},
            onNotificationButtonClick = {},
            selectedRecordFilterType = RecordFilterType.MONTHLY,
        )
    }
}
