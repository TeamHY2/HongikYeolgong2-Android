package com.teamhy2.feature.main.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray300
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Theme
import com.teamhy2.feature.main.MainTab

@Composable
fun MainBottomBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
) {
    val tabs = listOf(MainTab.HOME, MainTab.RECORD, MainTab.RANKING, MainTab.SETTING)

    BottomNavigation(
        backgroundColor = Gray800,
        modifier =
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
    ) {
        tabs.forEach { tab ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = tab.iconResId),
                        contentDescription = tab.contentDescription,
                        modifier = Modifier.size(28.dp),
                    )
                },
                label = {
                    Text(text = tab.contentDescription)
                },
                selected = tab == currentTab,
                selectedContentColor = Color.White,
                unselectedContentColor = Gray300,
                onClick = { onTabSelected(tab) },
                alwaysShowLabel = true,
                modifier =
                    Modifier
                        .padding(top = 12.dp)
                        .height(48.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainBottomBarPreview() {
    HY2Theme {
        val (currentTab, setCurrentTab) = remember { mutableStateOf(MainTab.HOME) }

        MainBottomBar(
            currentTab = currentTab,
            onTabSelected = { selectedTab ->
                setCurrentTab(selectedTab)
            },
        )
    }
}
