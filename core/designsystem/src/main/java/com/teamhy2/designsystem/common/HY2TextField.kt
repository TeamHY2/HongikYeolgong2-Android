package com.teamhy2.designsystem.common

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.R
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray400
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.Yellow300

private const val DEFAULT_BORDER_WIDTH = 1
private const val DEFAULT_RADIUS = 8

@Composable
fun HY2TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText: String = "",
    errorText: String = "",
    isEnabled: Boolean = true,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hasFocused by interactionSource.collectIsFocusedAsState()
    val transition = updateTransition(hasFocused, label = "onHasFocused")
    val borderColor by transition.animateColor(label = "borderColorTransition") { if (it) Gray400 else Gray800 }
    val focusRequester = remember { FocusRequester() }

    Column {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier =
                modifier
                    .background(
                        color = Gray800,
                        shape = RoundedCornerShape(DEFAULT_RADIUS.dp),
                    )
                    .height(48.dp)
                    .fillMaxWidth(),
            cursorBrush = SolidColor(Gray200),
            interactionSource = interactionSource,
            textStyle = HY2Typography().body05.copy(color = Gray200),
            keyboardOptions =
                KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction,
                ),
            keyboardActions = keyboardActions,
            enabled = isEnabled,
            singleLine = true,
        ) { innerTextField ->
            val modifierWithBorder =
                if (hasFocused) {
                    Modifier.border(
                        DEFAULT_BORDER_WIDTH.dp,
                        if (isError) Yellow300 else borderColor,
                        RoundedCornerShape(DEFAULT_RADIUS.dp),
                    )
                } else {
                    Modifier
                }
            Row(
                modifier =
                    modifierWithBorder
                        .background(
                            color = Gray800,
                            shape = RoundedCornerShape(DEFAULT_RADIUS.dp),
                        )
                        .focusRequester(focusRequester),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    if (!hasFocused && value.isBlank()) {
                        Text(
                            text = hintText,
                            style = MaterialTheme.typography.titleMedium,
                            color = Gray200,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    } else {
                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }
                    }
                    if (hasFocused && value.isNotBlank()) {
                        IconButton(onClick = { onValueChange("") }) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_all_remove),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (isError) errorText else "",
            style = HY2Typography().caption,
            color = Yellow300,
        )
    }
}

@Preview
@Composable
private fun HY2TextFieldPreview() {
    var value by remember { mutableStateOf("") }
    HY2Theme {
        HY2TextField(
            value = value,
            onValueChange = { value = it },
            hintText = "입력해주세요.",
        )
    }
}

@Preview
@Composable
private fun HY2TextFieldErrorPreview() {
    HY2Theme {
        HY2TextField(
            value = "abc",
            onValueChange = {},
            isError = true,
            errorText = "*올바른 형식의 이름이 아닙니다",
        )
    }
}
