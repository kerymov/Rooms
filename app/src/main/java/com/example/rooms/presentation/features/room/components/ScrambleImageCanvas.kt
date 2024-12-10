package com.example.rooms.presentation.features.room.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rooms.presentation.features.main.rooms.models.EventUi
import com.example.rooms.presentation.features.main.rooms.models.ScrambleUi
import com.example.rooms.presentation.theme.BlueCube
import com.example.rooms.presentation.theme.GreenCube
import com.example.rooms.presentation.theme.OrangeCube
import com.example.rooms.presentation.theme.RedCube
import com.example.rooms.presentation.theme.WhiteCube
import com.example.rooms.presentation.theme.YellowCube

private enum class ImageColor(val id: Int, val value: Color) {
    RED(0, RedCube),
    WHITE(1, WhiteCube),
    GREEN(2, GreenCube),
    ORANGE(3, OrangeCube),
    YELLOW(4, YellowCube),
    BLUE(5, BlueCube)
}

private const val FACES_IN_ROW = 4
private const val FACES_IN_COLUMN = 3
private val SPACE_BETWEEN_FACES = 2.dp
private val SPACE_INSIDE_FACE = 2.dp
private val FACE_INSET = 2.dp

@Composable
internal fun ScrambleImageCanvas(
    image: ScrambleUi.Image,
    event: EventUi,
    modifier: Modifier = Modifier
) {
    val redFace = image.faces.getOrNull(ImageColor.RED.id)
    val whiteFace = image.faces.getOrNull(ImageColor.WHITE.id)
    val greenFace = image.faces.getOrNull(ImageColor.GREEN.id)
    val orangeFace = image.faces.getOrNull(ImageColor.ORANGE.id)
    val yellowFace = image.faces.getOrNull(ImageColor.YELLOW.id)
    val blueFace = image.faces.getOrNull(ImageColor.BLUE.id)

    Canvas(modifier = modifier) {
        val faceSideLength = if (size.height < size.width) {
            (size.height - SPACE_BETWEEN_FACES.toPx() * 2) / FACES_IN_COLUMN.toFloat()
        } else {
            (size.width - SPACE_BETWEEN_FACES.toPx() * 3) / FACES_IN_ROW.toFloat()
        }

        val piecesInRow = event.id
        val faceInsets = FACE_INSET.toPx() * 2
        val faceInnerSpaces = SPACE_INSIDE_FACE.toPx() * (piecesInRow - 1)
        val pieceSideLength = (faceSideLength - faceInsets - faceInnerSpaces) / piecesInRow.toFloat()

        val faceSize = Size(faceSideLength, faceSideLength)
        val pieceSize = Size(pieceSideLength, pieceSideLength)

        val redFaceOffset = Offset(
            (faceSize.width + SPACE_BETWEEN_FACES.toPx()) * 2f,
            faceSize.height + SPACE_BETWEEN_FACES.toPx()
        )
        val whiteFaceOffset = Offset(
            faceSize.width + SPACE_BETWEEN_FACES.toPx(),
            0f
        )
        val greenFaceOffset = Offset(
            faceSize.width + SPACE_BETWEEN_FACES.toPx(),
            faceSize.height + SPACE_BETWEEN_FACES.toPx()
        )
        val orangeFaceOffset = Offset(
            0f,
            faceSize.height + SPACE_BETWEEN_FACES.toPx()
        )
        val yellowFaceOffset = Offset(
            faceSize.width + SPACE_BETWEEN_FACES.toPx(),
            (faceSize.height + SPACE_BETWEEN_FACES.toPx()) * 2f
        )
        val blueFaceOffset = Offset(
            (faceSize.width + SPACE_BETWEEN_FACES.toPx()) * 3f,
            faceSize.height + SPACE_BETWEEN_FACES.toPx()
        )

        whiteFace?.let {
            drawFace(
                face = it,
                faceSize = faceSize,
                pieceSize = pieceSize,
                offset = whiteFaceOffset
            )
        }
        orangeFace?.let {
            drawFace(
                face = it,
                faceSize = faceSize,
                pieceSize = pieceSize,
                offset = orangeFaceOffset
            )
        }
        greenFace?.let {
            drawFace(
                face = it,
                faceSize = faceSize,
                pieceSize = pieceSize,
                offset = greenFaceOffset
            )
        }
        redFace?.let {
            drawFace(
                face = it,
                faceSize = faceSize,
                pieceSize = pieceSize,
                offset = redFaceOffset
            )
        }
        blueFace?.let {
            drawFace(
                face = it,
                faceSize = faceSize,
                pieceSize = pieceSize,
                offset = blueFaceOffset
            )
        }
        yellowFace?.let {
            drawFace(
                face = it,
                faceSize = faceSize,
                pieceSize = pieceSize,
                offset = yellowFaceOffset
            )
        }
    }
}

private fun DrawScope.drawFace(
    face: ScrambleUi.Face,
    faceSize: Size,
    pieceSize: Size,
    offset: Offset
) {
    translate(left = offset.x, top = offset.y) {
        drawRect(color = Color.Black, size = faceSize)

        inset(vertical = FACE_INSET.toPx(), horizontal = FACE_INSET.toPx()) {
            face.colors.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, colorId ->
                    val leftOffset = (pieceSize.width + SPACE_INSIDE_FACE.toPx()) * columnIndex.toFloat()
                    val topOffset = (pieceSize.height  + SPACE_INSIDE_FACE.toPx()) * rowIndex.toFloat()
                    val topLeftOffset = Offset(x = leftOffset, y = topOffset)

                    drawRect(color = colorId.toColor(), size = pieceSize, topLeft = topLeftOffset)
                }
            }
        }
    }
}

private fun Int.toColor() = ImageColor.entries.find { it.id == this }?.value ?: Color.Gray

@Preview
@Composable
private fun ScrambleImageCanvasWidePreview() {
    ScrambleImageCanvas(
        image = image,
        event = EventUi.THREE_BY_THREE,
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    )
}

@Preview
@Composable
private fun ScrambleImageCanvasHighPreview() {
    ScrambleImageCanvas(
        image = image,
        event = EventUi.THREE_BY_THREE,
        modifier = Modifier
            .width(180.dp)
            .height(320.dp)
    )
}

private val image = ScrambleUi.Image(
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