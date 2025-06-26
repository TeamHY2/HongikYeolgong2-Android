package com.teamhy2.designsystem.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.HY2Theme

@Composable
fun ColumnScope.HY2Spacer(height: Int) {
    Spacer(Modifier.height(height.dp))
}

@Composable
fun RowScope.HY2Spacer(width: Int) {
    Spacer(Modifier.width(width.dp))
}

@Preview
@Composable
private fun ColumnScopeHY2SpacerPreview() {
    HY2Theme {
        Column {
            Text("안녕하세요")
            HY2Spacer(50)
            Text("안녕하세요")
        }
    }
}

@Preview
@Composable
private fun RowScopeHY2SpacerPreview() {
    HY2Theme {
        Row {
            Text("안녕하세요")
            HY2Spacer(50)
            Text("안녕하세요")
        }
    }
}
