package com.example.ui_room.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui_core.theme.BlueCube
import com.example.ui_core.theme.GreenCube
import com.example.ui_core.theme.OrangeCube
import com.example.ui_core.theme.RedCube
import com.example.ui_core.theme.WhiteCube
import com.example.ui_core.theme.YellowCube
import com.example.ui_common_speedcubing.models.EventUi
import com.example.ui_common_speedcubing.models.ScrambleUi

private enum class CubeColor(val id: Int, val value: Color) {
    RED(0, RedCube),
    WHITE(1, WhiteCube),
    GREEN(2, GreenCube),
    ORANGE(3, OrangeCube),
    YELLOW(4, YellowCube),
    BLUE(5, BlueCube)
}

@Composable
fun ScrambleImage(
    image: ScrambleUi.Image,
    event: EventUi,
    modifier: Modifier = Modifier
) {
    val redFace = image.faces.getOrNull(CubeColor.RED.id)
    val whiteFace = image.faces.getOrNull(CubeColor.WHITE.id)
    val greenFace = image.faces.getOrNull(CubeColor.GREEN.id)
    val orangeFace = image.faces.getOrNull(CubeColor.ORANGE.id)
    val yellowFace = image.faces.getOrNull(CubeColor.YELLOW.id)
    val blueFace = image.faces.getOrNull(CubeColor.BLUE.id)

    val columnsCount = event.id
    val faceModifier = Modifier
        .size(68.dp)
        .wrapContentSize()

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FaceItem(modifier = faceModifier)
            whiteFace?.let { FaceItem(colors = whiteFace.colors, columnsCount = columnsCount, modifier = faceModifier) }
            FaceItem(modifier = faceModifier)
            FaceItem(modifier = faceModifier)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            orangeFace?.let { FaceItem(colors = orangeFace.colors, columnsCount = columnsCount, modifier = faceModifier) }
            greenFace?.let { FaceItem(colors = greenFace.colors, columnsCount = columnsCount, modifier = faceModifier) }
            redFace?.let { FaceItem(colors = redFace.colors, columnsCount = columnsCount, modifier = faceModifier) }
            blueFace?.let { FaceItem(colors = blueFace.colors, columnsCount = columnsCount, modifier = faceModifier) }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FaceItem(modifier = faceModifier)
            yellowFace?.let { FaceItem(colors = yellowFace.colors, columnsCount = columnsCount, modifier = faceModifier) }
            FaceItem(modifier = faceModifier)
            FaceItem(modifier = faceModifier)
        }
    }
}

@Composable
private fun FaceItem(modifier: Modifier) = Box(modifier = modifier)

@Composable
private fun FaceItem(colors: List<List<Int>>, columnsCount: Int, modifier: Modifier) = LazyVerticalGrid(
    columns = GridCells.Fixed(columnsCount),
    verticalArrangement = Arrangement.spacedBy(2.dp),
    horizontalArrangement = Arrangement.spacedBy(2.dp),
    contentPadding = PaddingValues(2.dp),
    userScrollEnabled = false,
    modifier = modifier.background(Color.Black)
) {
    items(colors.flatten()) { color ->
        ColorItem(color.toColor())
    }
}

@Composable
private fun Int.toColor() = CubeColor.entries.find { it.id == this }?.value ?: Color.Gray

@Composable
private fun ColorItem(
    color: Color
) = Box(
    modifier = Modifier
        .size(20.dp)
        .background(color)
)

@Preview
@Composable
private fun ScrambleImagePreview() {
    ScrambleImage(
        event = EventUi.THREE_BY_THREE,
        image = ScrambleUi.Image(
            faces = listOf(
                ScrambleUi.Face(
                    colors = listOf(
                        listOf(2, 4, 5),
                        listOf(3, 0, 4),
                        listOf(0, 4, 4)
                    )
                ),
                ScrambleUi.Face(
                    colors = listOf(
                        listOf(0, 5, 1),
                        listOf(0, 1, 0),
                        listOf(3, 2, 0)
                    )
                ),
                ScrambleUi.Face(
                    colors = listOf(
                        listOf(4, 1, 1),
                        listOf(2, 2, 5),
                        listOf(2, 4, 5)
                    )
                ),
                ScrambleUi.Face(
                    colors = listOf(
                        listOf(5, 2, 5),
                        listOf(3, 3, 3),
                        listOf(4, 0, 1)
                    )
                ),
                ScrambleUi.Face(
                    colors = listOf(
                        listOf(3, 3, 1),
                        listOf(1, 4, 5),
                        listOf(2, 1, 2)
                    )
                ),
                ScrambleUi.Face(
                    colors = listOf(
                        listOf(3, 0, 4),
                        listOf(2, 5, 1),
                        listOf(3, 5, 0)
                    )
                )
            )
        )
    )
}