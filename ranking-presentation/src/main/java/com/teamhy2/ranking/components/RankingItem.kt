package com.teamhy2.ranking.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.designsystem.ui.theme.Gray100
import com.teamhy2.designsystem.ui.theme.Gray600
import com.teamhy2.designsystem.ui.theme.Gray800
import com.teamhy2.designsystem.ui.theme.HY2Typography
import com.teamhy2.designsystem.ui.theme.White
import com.teamhy2.hongikyeolgong2.ranking.presentation.R.drawable.ic_ranking_dash
import com.teamhy2.hongikyeolgong2.ranking.presentation.R.drawable.ic_ranking_down
import com.teamhy2.hongikyeolgong2.ranking.presentation.R.drawable.ic_ranking_up
import com.teamhy2.hongikyeolgong2.ranking.presentation.R.drawable.ic_ranking_up_first
import com.teamhy2.hongikyeolgong2.ranking.presentation.R.drawable.img_ranking_background_first
import com.teamhy2.hongikyeolgong2.ranking.presentation.R.drawable.img_ranking_background_second
import com.teamhy2.hongikyeolgong2.ranking.presentation.R.drawable.img_ranking_background_third
import com.teamhy2.ranking.components.RankItemStyle.FIRST
import com.teamhy2.ranking.components.RankItemStyle.OTHER
import com.teamhy2.ranking.components.RankItemStyle.SECOND
import com.teamhy2.ranking.components.RankItemStyle.THIRD

enum class RankItemStyle(
    @DrawableRes val backgroundImageId: Int?,
    val textColor: Color,
) {
    FIRST(img_ranking_background_first, Gray600),
    SECOND(img_ranking_background_second, White),
    THIRD(img_ranking_background_third, Gray100),
    OTHER(null, Gray100),
}

@Composable
fun RankingItem(
    rank: Int,
    department: String,
    hours: String,
    rankChange: Int,
    modifier: Modifier = Modifier,
) {
    val rankItemStyle =
        when (rank) {
            1 -> FIRST
            2 -> SECOND
            3 -> THIRD
            else -> OTHER
        }

    @DrawableRes val rankChangeIcon =
        when {
            rankChange > 0 && rank == 1 -> ic_ranking_up_first
            rankChange > 0 -> ic_ranking_up
            rankChange < 0 -> ic_ranking_down
            else -> ic_ranking_dash
        }

    val rankChangeText =
        if (rankChange > 0) {
            "+$rankChange"
        } else if (rankChange < 0) {
            "$rankChange"
        } else {
            ""
        }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .let {
                    if (rankItemStyle.backgroundImageId != null) {
                        it
                            .paint(
                                painter = painterResource(id = rankItemStyle.backgroundImageId),
                                contentScale = ContentScale.FillBounds,
                            )
                            .padding(horizontal = 24.dp, vertical = 13.dp)
                    } else {
                        it
                            .background(Gray800, shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 24.dp, vertical = 17.dp)
                    }
                },
    ) {
        Text(
            text = "$rank",
            style = HY2Typography().body05,
            color = rankItemStyle.textColor,
        )
        Spacer(modifier = Modifier.width(18.dp))
        Text(
            text = department,
            style = HY2Typography().body05,
            color = rankItemStyle.textColor,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = hours,
            style = HY2Typography().body05,
            color = rankItemStyle.textColor,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.width(50.dp),
        ) {
            Text(
                text = rankChangeText,
                style = HY2Typography().body05,
                color = rankItemStyle.textColor,
            )
            Spacer(modifier = Modifier.width(7.dp))
            Image(
                painter = painterResource(id = rankChangeIcon),
                contentDescription = null,
                modifier = Modifier.size(10.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RankingItemFirstPreview() {
    RankingItem(
        rank = 1,
        department = "국어국문학과",
        hours = "200H",
        rankChange = 1,
    )
}

@Preview(showBackground = true)
@Composable
fun RankingItemSecondPreview() {
    RankingItem(
        rank = 2,
        department = "디자인학부",
        hours = "170H",
        rankChange = 1,
    )
}

@Preview(showBackground = true)
@Composable
fun RankingItemThirdPreview() {
    RankingItem(
        rank = 3,
        department = "경영학부",
        hours = "120H",
        rankChange = -1,
    )
}

@Preview(showBackground = true)
@Composable
fun RankingItemOtherPreview() {
    RankingItem(
        rank = 4,
        department = "건축학부",
        hours = "100H",
        rankChange = 0,
    )
}
