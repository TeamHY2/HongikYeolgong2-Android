package com.teamhy2.designsystem.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray400
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.util.modifier.crop

private const val MAX_VISIBLE_ITEM_COUNT = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HY2DropdownTextField(
    options: List<String>,
    hintText: String,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    ExposedDropdownMenuBox(
        modifier = modifier.wrapContentWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        HY2TextField(
            value = value,
            onValueChange = { text -> if (text != value) onValueChanged(text) },
            hintText = hintText,
            focusRequester = focusRequester,
            modifier = Modifier.menuAnchor(),
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {},
            properties = PopupProperties(focusable = false),
            offset = DpOffset(x = 0.dp, y = (-16).dp),
            modifier =
                Modifier
                    .crop(vertical = 8.dp)
                    .heightIn(max = (MAX_VISIBLE_ITEM_COUNT * 48 + 12).dp)
                    .background(Gray800)
                    .clip(RoundedCornerShape(8.dp))
                    .exposedDropdownSize(),
        ) {
            options.filter { it.contains(value) }.forEach { selectionOption ->
                HY2DropdownMenuItem(
                    value = selectionOption,
                    isSelected = selectionOption == value,
                    modifier =
                        Modifier.clickable {
                            expanded = false
                            onValueChanged(selectionOption)
                            focusManager.clearFocus()
                        },
                )
            }
        }
    }
}

@Composable
private fun HY2DropdownMenuItem(
    value: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .background(color = if (isSelected) Gray400 else Gray800)
                .height(40.dp)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = value,
            style = HY2Typography().body05,
            color = Gray200,
        )
    }
}

@Preview
@Composable
private fun HY2DropdownTextFieldPreview() {
    var value by remember { mutableStateOf("") }
    val list = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    HY2Theme {
        HY2DropdownTextField(
            options = list,
            value = value,
            onValueChanged = { value = it },
            hintText = "입력해주세요.",
        )
    }
}
