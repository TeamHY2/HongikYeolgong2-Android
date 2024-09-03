package com.teamhy2.feature.main.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.teamhy2.hongikyeolgong2.main.presentation.R

@Composable
fun StarsComponent(
    starCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        Image(
            painter = getStarPainter(starCount, 1),
            contentDescription = null,
        )
        Image(
            painter = getStarPainter(starCount, 2),
            contentDescription = null,
        )
        Image(
            painter = getStarPainter(starCount, 3),
            contentDescription = null,
        )
    }
}

@Composable
fun getStarPainter(
    starCount: Int,
    position: Int,
): Painter {
    return when (position) {
        1 -> {
            if (starCount >= 1) {
                painterResource(id = R.drawable.ic_star_1)
            } else {
                painterResource(id = R.drawable.ic_star_0)
            }
        }

        2 -> {
            if (starCount >= 2) {
                painterResource(id = R.drawable.ic_star_2)
            } else {
                painterResource(id = R.drawable.ic_star_0)
            }
        }

        3 -> {
            if (starCount >= 3) {
                painterResource(id = R.drawable.ic_star_3)
            } else {
                painterResource(id = R.drawable.ic_star_0)
            }
        }

        else -> painterResource(id = R.drawable.ic_star_0)
    }
}

@Preview(showBackground = true)
@Composable
private fun StarCount1ComponentPreview() {
    StarsComponent(starCount = 1)
}

@Preview(showBackground = true)
@Composable
private fun StarsCount2ComponentPreview() {
    StarsComponent(starCount = 2)
}

@Preview(showBackground = true)
@Composable
private fun StarsCount3ComponentPreview() {
    StarsComponent(starCount = 3)
}
