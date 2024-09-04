package com.example.rooms.presentation.features.rooms.components

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
import com.example.rooms.data.model.scramble.FaceDto
import com.example.rooms.data.model.scramble.ImageDto
import com.example.rooms.presentation.theme.BlueCube
import com.example.rooms.presentation.theme.GreenCube
import com.example.rooms.presentation.theme.OrangeCube
import com.example.rooms.presentation.theme.RedCube
import com.example.rooms.presentation.theme.WhiteCube
import com.example.rooms.presentation.theme.YellowCube

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
    image: ImageDto,
    modifier: Modifier = Modifier
) {
    val redFace = image.faces.getOrNull(CubeColor.RED.id)
    val whiteFace = image.faces.getOrNull(CubeColor.WHITE.id)
    val greenFace = image.faces.getOrNull(CubeColor.GREEN.id)
    val orangeFace = image.faces.getOrNull(CubeColor.ORANGE.id)
    val yellowFace = image.faces.getOrNull(CubeColor.YELLOW.id)
    val blueFace = image.faces.getOrNull(CubeColor.BLUE.id)

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
            whiteFace?.let { FaceItem(colors = whiteFace.colors, modifier = faceModifier) }
            FaceItem(modifier = faceModifier)
            FaceItem(modifier = faceModifier)
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            orangeFace?.let { FaceItem(colors = orangeFace.colors, modifier = faceModifier) }
            greenFace?.let { FaceItem(colors = greenFace.colors, modifier = faceModifier) }
            redFace?.let { FaceItem(colors = redFace.colors, modifier = faceModifier) }
            blueFace?.let { FaceItem(colors = blueFace.colors, modifier = faceModifier) }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FaceItem(modifier = faceModifier)
            yellowFace?.let { FaceItem(colors = yellowFace.colors, modifier = faceModifier) }
            FaceItem(modifier = faceModifier)
            FaceItem(modifier = faceModifier)
        }
    }
}

@Composable
private fun FaceItem(modifier: Modifier) = Box(modifier = modifier)

@Composable
private fun FaceItem(colors: List<List<Int>>, modifier: Modifier) = LazyVerticalGrid(
    columns = GridCells.Fixed(3),
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
        image = ImageDto(
            faces = listOf(
                FaceDto(
                    colors = listOf(
                        listOf(2, 4, 5),
                        listOf(3, 0, 4),
                        listOf(0, 4, 4)
                    )
                ),
                FaceDto(
                    colors = listOf(
                        listOf(0, 5, 1),
                        listOf(0, 1, 0),
                        listOf(3, 2, 0)
                    )
                ),
                FaceDto(
                    colors = listOf(
                        listOf(4, 1, 1),
                        listOf(2, 2, 5),
                        listOf(2, 4, 5)
                    )
                ),
                FaceDto(
                    colors = listOf(
                        listOf(5, 2, 5),
                        listOf(3, 3, 3),
                        listOf(4, 0, 1)
                    )
                ),
                FaceDto(
                    colors = listOf(
                        listOf(3, 3, 1),
                        listOf(1, 4, 5),
                        listOf(2, 1, 2)
                    )
                ),
                FaceDto(
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