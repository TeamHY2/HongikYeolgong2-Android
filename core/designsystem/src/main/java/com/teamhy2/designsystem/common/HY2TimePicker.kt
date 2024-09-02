package com.teamhy2.designsystem.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.teamhy2.designsystem.R
import com.teamhy2.designsystem.ui.theme.Blue100
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.designsystem.util.pixelsToDp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import java.time.ZoneId
import java.util.Locale

private const val DIALOG_MARGIN = 22
private const val DIALOG_CORNER_RADIUS = 8
private const val DIALOG_BACKGROUND_DIM_AMOUNT = 0.75f
private const val TIME_PICKER_NUMBER_FORMAT = "%02d"
private const val PICKER_DEFAULT_VISIBLE_ITEMS_COUNT = 3

@Composable
fun HY2TimePicker(
    title: String,
    onSelected: (LocalTime) -> Unit,
    onCancelled: () -> Unit,
    modifier: Modifier = Modifier,
    localtime: LocalTime = LocalTime.now(ZoneId.of("Asia/Seoul")),
    onDismiss: () -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
    ) {
        (LocalView.current.parent as DialogWindowProvider)
            .window
            .setDimAmount(DIALOG_BACKGROUND_DIM_AMOUNT)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier =
                modifier
                    .width(screenWidth - DIALOG_MARGIN.dp * 2)
                    .wrapContentHeight()
                    .background(Gray800, RoundedCornerShape(DIALOG_CORNER_RADIUS.dp)),
        ) {
            val hours: List<String> =
                remember {
                    (1..localtime.hour).map { String.format(Locale.KOREA, TIME_PICKER_NUMBER_FORMAT, it) }
                }
            val hourState: PickerState = rememberPickerState()
            val minutes: List<String> =
                remember {
                    (0..59).map { String.format(Locale.KOREA, TIME_PICKER_NUMBER_FORMAT, it) }
                }
            val minuteState: PickerState = rememberPickerState()
            val meridiem: List<String> =
                remember {
                    if (localtime.hour < 12) listOf("AM") else listOf("AM", "PM")
                }
            val meridiemState: PickerState = rememberPickerState()

            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = title,
                style = HY2Typography().title02,
                color = Gray100,
            )
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 68.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Picker(
                    state = hourState,
                    items = hours,
                    visibleItemsCount = 3,
                    startIndex =
                        hours.indexOf(
                            String.format(
                                Locale.KOREA,
                                TIME_PICKER_NUMBER_FORMAT,
                                ((localtime.hour) % 12),
                            ),
                        ),
                    modifier = Modifier.weight(0.33f),
                    textModifier = Modifier.padding(8.dp),
                )
                Text(
                    text = ":",
                    style = HY2Typography().title01,
                    color = White,
                )
                Picker(
                    state = minuteState,
                    items = minutes,
                    visibleItemsCount = 3,
                    startIndex = minutes.indexOf(localtime.minute.toString()),
                    modifier = Modifier.weight(0.33f),
                    textModifier = Modifier.padding(8.dp),
                )
                Picker(
                    state = meridiemState,
                    items = meridiem,
                    visibleItemsCount = 1,
                    startIndex = if (localtime.hour <= 12) 0 else 1,
                    modifier = Modifier.weight(0.33f),
                    textModifier = Modifier.padding(8.dp),
                )
            }
            Spacer(modifier = Modifier.height(42.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
            ) {
                HY2DialogButton(
                    text = stringResource(R.string.time_picker_cancel),
                    onClick = onCancelled,
                    buttonColor = Gray600,
                    textColor = Gray200,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(12.dp))
                HY2DialogButton(
                    text = stringResource(R.string.time_picker_confirm),
                    onClick = {
                        onSelected(
                            LocalTime.of(
                                hourState.selectedItem.toInt() % 12,
                                minuteState.selectedItem.toInt(),
                            ).plusHours(if (meridiemState.selectedItem == "AM") 0L else 12L),
                        )
                    },
                    buttonColor = Blue100,
                    textColor = White,
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Picker(
    items: List<String>,
    modifier: Modifier = Modifier,
    state: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int = PICKER_DEFAULT_VISIBLE_ITEMS_COUNT,
    textModifier: Modifier = Modifier,
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.intValue)

    val fadingEdgeGradient =
        remember {
            Brush.verticalGradient(
                0f to Color.Transparent,
                0.5f to Color.Black,
                1f to Color.Transparent,
            )
        }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(itemHeightDp * visibleItemsCount)
                    .fadingEdge(fadingEdgeGradient),
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    style = HY2Typography().title01,
                    color = White,
                    modifier =
                        Modifier
                            .onSizeChanged { size -> itemHeightPixels.intValue = size.height }
                            .then(textModifier),
                )
            }
        }
    }
}

private fun Modifier.fadingEdge(brush: Brush) =
    this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()
            drawRect(brush = brush, blendMode = BlendMode.DstIn)
        }

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
}

@Preview
@Composable
private fun HY2TimePickerPreview() {
    HY2Theme(darkTheme = true) {
        HY2TimePicker(
            title = "열람실 이용 시간",
            onSelected = {},
            onCancelled = {},
            onDismiss = {},
        )
    }
}
