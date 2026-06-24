package com.teamhy2.friend.search.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray200
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.hongikyeolgong2.friend.presentation.R

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_calendar_left),
            contentDescription = "back",
            modifier =
                Modifier
                    .size(24.dp)
                    .clickable { onBackClick() },
            tint = Gray200,
        )
        Spacer(Modifier.width(7.dp))
        Row(
            modifier =
                Modifier
                    .weight(1f)
                    .height(48.dp)
                    .background(color = Gray800, shape = RoundedCornerShape(8.dp))
                    .padding(start = 16.dp, end = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit_text_search),
                contentDescription = "search",
                modifier = Modifier.size(24.dp),
                tint = Gray200,
            )
            TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                placeholder = {
                    Text(
                        text = "친구를 검색해보세요",
                        style = HY2Theme.typography.body05,
                        color = Gray300,
                    )
                },
                trailingIcon = {
                    if (query.isNotBlank()) {
                        IconButton(onClick = onClearQuery) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_edit_text_close),
                                contentDescription = "clear",
                            )
                        }
                    }
                },
                singleLine = true,
                colors =
                    TextFieldDefaults.colors(
                        focusedTextColor = Gray100,
                        unfocusedTextColor = Gray100,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedPlaceholderColor = Gray800,
                        unfocusedPlaceholderColor = Gray800,
                    ),
                textStyle = HY2Typography().body05,
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun SearchBarPreview() {
    var text by remember { mutableStateOf("") }

    HY2Theme {
        SearchBar(
            query = text,
            onQueryChange = { text = it },
            onClearQuery = { text = "" },
            onBackClick = {},
        )
    }
}
