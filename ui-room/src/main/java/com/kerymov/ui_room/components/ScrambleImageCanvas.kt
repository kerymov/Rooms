package com.kerymov.ui_room.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kerymov.ui_common_speedcubing.models.EventUi
import com.kerymov.ui_common_speedcubing.models.ScrambleUi
import com.kerymov.ui_core.theme.BlueCube
import com.kerymov.ui_core.theme.GreenCube
import com.kerymov.ui_core.theme.OrangeCube
import com.kerymov.ui_core.theme.RedCube
import com.kerymov.ui_core.theme.WhiteCube
import com.kerymov.ui_core.theme.YellowCube
import com.kerymov.ui_room.utils.CanvasContentScale
import com.kerymov.ui_room.utils.calculateContentZoom

private interface Drawable {
    val offset: Offset
}

private data class DrawableFace(
    val face: ScrambleUi.Face, override val offset: Offset
) : Drawable

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
private const val ASPECT_RATIO = FACES_IN_ROW.toFloat() / FACES_IN_COLUMN.toFloat()
private val PIECE_SIDE_LENGTH = 40.dp
private val SPACE_BETWEEN_FACES = 4.dp
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

    val piecesInRow = event.id

    Box(
        modifier = modifier
            .requiredSizeIn(92.dp, 69.dp)
            .defaultMinSize(92.dp, 69.dp)
            .aspectRatio(ASPECT_RATIO)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .drawBehind {
                val contentWidth = PIECE_SIDE_LENGTH.toPx() * piecesInRow +
                        SPACE_INSIDE_FACE.toPx() * (piecesInRow - 1) * 4f +
                        FACE_INSET.toPx() * 2f * 4f +
                        SPACE_BETWEEN_FACES.toPx() * 3f
                val contentHeight = contentWidth / ASPECT_RATIO

                val contentRect = Rect(
                    offset = Offset.Zero,
                    size = Size(contentWidth, contentHeight)
                )
                val matrix = Matrix().apply {
                    translate(size.center.x, size.center.y)
                    val zoom = calculateContentZoom(
                        canvasSize = size,
                        contentSize = contentRect.size,
                        contentScale = CanvasContentScale.CENTER_FIT
                    )
                    scale(zoom, zoom)
                    translate(-contentRect.center.x, -contentRect.center.y)
                }

                val transformedSpaceBetweenFaces = SPACE_BETWEEN_FACES.toPx()
                val transformedSpaceInsideFace = SPACE_INSIDE_FACE.toPx()
                val transformedFaceInsent = FACE_INSET.toPx()

                val faceSideLength =
                    (contentRect.height - transformedSpaceBetweenFaces * 2) / FACES_IN_COLUMN.toFloat()

                val faceInsets = transformedFaceInsent * 2
                val faceInnerSpaces = transformedSpaceInsideFace * (piecesInRow - 1)
                val pieceSideLength =
                    (faceSideLength - faceInsets - faceInnerSpaces) / piecesInRow.toFloat()

                val faceSize = Size(faceSideLength, faceSideLength)
                val pieceSize = Size(pieceSideLength, pieceSideLength)

                val redFaceOffset = Offset(
                    (faceSize.width + transformedSpaceBetweenFaces) * 2f,
                    faceSize.height + transformedSpaceBetweenFaces
                )
                val whiteFaceOffset = Offset(
                    faceSize.width + transformedSpaceBetweenFaces,
                    0f
                )
                val greenFaceOffset = Offset(
                    faceSize.width + transformedSpaceBetweenFaces,
                    faceSize.height + transformedSpaceBetweenFaces
                )
                val orangeFaceOffset = Offset(
                    0f,
                    faceSize.height + transformedSpaceBetweenFaces
                )
                val yellowFaceOffset = Offset(
                    faceSize.width + transformedSpaceBetweenFaces,
                    (faceSize.height + transformedSpaceBetweenFaces) * 2f
                )
                val blueFaceOffset = Offset(
                    (faceSize.width + transformedSpaceBetweenFaces) * 3f,
                    faceSize.height + transformedSpaceBetweenFaces
                )

                val drawableFaces = listOfNotNull(
                    redFace?.let { DrawableFace(it, redFaceOffset) },
                    whiteFace?.let { DrawableFace(it, whiteFaceOffset) },
                    greenFace?.let { DrawableFace(it, greenFaceOffset) },
                    orangeFace?.let { DrawableFace(it, orangeFaceOffset) },
                    yellowFace?.let { DrawableFace(it, yellowFaceOffset) },
                    blueFace?.let { DrawableFace(it, blueFaceOffset) }
                )

                withTransform(
                    transformBlock = {
                        transform(matrix)
                    },
                    drawBlock = {
                        drawableFaces.forEach {
                            drawFace(
                                face = it.face,
                                faceSize = faceSize,
                                pieceSize = pieceSize,
                                faceInset = transformedFaceInsent,
                                spaceInsideFace = transformedSpaceInsideFace,
                                offset = it.offset
                            )
                        }
                    }
                )
            }
    )
}

private fun DrawScope.drawFace(
    face: ScrambleUi.Face,
    faceSize: Size,
    pieceSize: Size,
    faceInset: Float,
    spaceInsideFace: Float,
    offset: Offset
) {
    translate(left = offset.x, top = offset.y) {
        drawRect(color = Color.Black, size = faceSize)

        inset(vertical = faceInset, horizontal = faceInset) {
            face.colors.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, colorId ->
                    val leftOffset = (pieceSize.width + spaceInsideFace) * columnIndex.toFloat()
                    val topOffset = (pieceSize.height  + spaceInsideFace) * rowIndex.toFloat()
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
private fun ScrambleImageCanvasHighPreview() {
    ScrambleImageCanvas(
        image = image,
        event = EventUi.THREE_BY_THREE,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Preview
@Composable
private fun ScrambleImageCanvasWidePreview() {
    ScrambleImageCanvas(
        image = image,
        event = EventUi.THREE_BY_THREE,
        modifier = Modifier.height(72.dp)
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