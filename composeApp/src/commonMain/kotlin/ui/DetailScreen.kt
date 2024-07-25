package ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.TextLine
import processor.FlameChartData
import processor.TraceNode

@Composable
fun DetailScreen(component: FlameChartData, goBack: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("GoBack", modifier = Modifier.clickable { goBack.invoke() })
        Spacer(Modifier.height(60.dp))

        val stateVertical = rememberScrollState(0)
        val stateHorizontal = rememberScrollState(0)

        val scaleWidthFactor = remember { mutableFloatStateOf(1F) }
        val scaleHeightFactor = remember { mutableFloatStateOf(50F) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
                .horizontalScroll(stateHorizontal)
        ) {

            FlameChart(
                component,
                scaleHeightFactor = scaleHeightFactor.value,
                scaleWidthFactor = scaleWidthFactor.value
            )
        }
    }
}

@Composable
fun FlameChart(component: FlameChartData, scaleHeightFactor: Float, scaleWidthFactor: Float) {
    val canvasWidth = ((component.lastCaptureTime - component.firstCaptureTime) * scaleWidthFactor).toDp
    val canvasHeight = (component.flameMap.size * scaleHeightFactor).toDp
    println("canvasWidth: $canvasWidth, canvasHeight $canvasHeight")
    println("lastCaptureTime: ${component.lastCaptureTime}, firstCaptureTime ${component.firstCaptureTime}")
    Column(
        Modifier
            .padding(16.dp)
            .drawBehind {
                drawAxis(
                    firstCaptureTime = component.firstCaptureTime,
                    lastCaptureTime = component.lastCaptureTime,
                    scaleWidthFactor = scaleWidthFactor,
                    height = (component.flameMap.size * scaleHeightFactor) + 40F
                )
            }
            .height(canvasHeight)
            .width(canvasWidth)
    ) {
        for ((key, value) in component.flameMap) {
            DrawFunctionRow(
                level = key,
                functions = value,
                scaleHeightFactor = scaleHeightFactor,
                scaleWidthFactor = scaleWidthFactor
            )
        }
    }
}

internal fun DrawScope.drawAxis(
    firstCaptureTime: Long,
    lastCaptureTime: Long,
    scaleWidthFactor: Float,
    height: Float
) {
    val totalWidth = lastCaptureTime - firstCaptureTime
    val totalScale = (totalWidth / 100) + 2

    println("firstCaptureTime $firstCaptureTime, lastCaptureTime $lastCaptureTime, scaleWidthFactor $scaleWidthFactor  totalScale: $totalScale  totalWidth $totalWidth")

    val textPaint = Paint().apply {
        isAntiAlias = true
    }
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f, 20f), 0f)


    repeat(totalScale.toInt()) { i ->
        drawIntoCanvas {
            val text = "${i * 100} ms"
            val textLine = TextLine.make(
                text, Font(
                    typeface = null,
                    size = 20f,
                )
            )
            val textLineWidthHalf = textLine.width / 2F
            val textStartX = ((i * 100) * scaleWidthFactor) - textLineWidthHalf

            it.nativeCanvas.apply {

                drawTextLine(
                    textLine,
                    textStartX,
                    0F,
                    textPaint
                )

                if (i != 0) {
                    drawLine(
                        start = Offset(x = textStartX, y = 0f),
                        end = Offset(x = textStartX, y = height),
                        color = Color.DarkGray,
                        pathEffect = pathEffect,
                        alpha = 0.5F,
                        strokeWidth = 1F
                    )
                }
            }
        }
    }
}

@Composable
fun DrawFunctionRow(
    level: Int,
    functions: List<TraceNode>,
    scaleHeightFactor: Float,
    scaleWidthFactor: Float,
    modifier: Modifier = Modifier,
) {
    Row {
        functions.forEach {
            drawFunctionCall(
                modifier = modifier,
                startX = it.firstDrawBoundary * scaleWidthFactor,
                startY = (level + 1) * scaleHeightFactor,
                name = it.functionName,
                width = (it.lastDrawBoundary - it.firstDrawBoundary) * scaleWidthFactor,
                heightFactor = scaleHeightFactor,
            )
        }
    }
}

@Composable
fun drawFunctionCall(
    modifier: Modifier = Modifier,
    startX: Float,
    startY: Float,
    name: String,
    width: Float,
    heightFactor: Float
) {
    Canvas(modifier) {
        drawRect(
            topLeft = Offset(x = startX, y = startY),
            color = Color.Black,
            style = Stroke(2F),
            size = Size(width, heightFactor)
        )
        drawRect(
            topLeft = Offset(x = startX, y = startY),
            color = Color(0xE4FF0000),
            size = Size(width, heightFactor)
        )
        drawFunctionName(
            name = name,
            startX = startX,
            width = width,
            startY = startY,
            heightFactor = heightFactor
        )
    }
}

fun DrawScope.drawFunctionName(name: String, startX: Float, width: Float, startY: Float, heightFactor: Float) {
    drawIntoCanvas {
        val textLine = TextLine.make(
            name,
            Font(
                typeface = null,
                size = 20f,
            )
        )
        val textLineWidthHalf = textLine.width / 2F
        val textLineHeightHalf = textLine.height / 2F

        val textStartX = (startX + width / 2F) - textLineWidthHalf
        val textStartY = (startY + heightFactor / 2F) + textLineHeightHalf
        it.nativeCanvas.apply {
            clipRect(Rect(startX + 10, startY, startX + width - 10, startY + heightFactor))
            drawTextLine(
                line = textLine,
                x = textStartX,
                y = textStartY,
                paint = Paint()
            )
        }
    }
}

inline val Float.toDp: Dp
    @Composable get() = with(LocalDensity.current) { this@toDp.toDp() }