import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamhy2.hongikyeolgong2.main.presentation.R

private const val STAR_IMAGE_SIZE = 40

@Composable
fun StarsComponent(
    starCount: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
    ) {
        repeat(3) { position ->
            Image(
                painter = getStarPainter(starCount, position + 1),
                contentDescription = null,
                modifier = Modifier.size(STAR_IMAGE_SIZE.dp),
            )
        }
    }
}

@Composable
fun getStarPainter(
    starCount: Int,
    position: Int,
): Painter {
    return when {
        starCount >= position -> painterResource(id = getStarDrawableId(position))
        else -> painterResource(id = R.drawable.ic_star_0)
    }
}

private fun getStarDrawableId(position: Int): Int {
    return when (position) {
        1 -> R.drawable.ic_star_1
        2 -> R.drawable.ic_star_2
        3 -> R.drawable.ic_star_3
        else -> R.drawable.ic_star_0
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
