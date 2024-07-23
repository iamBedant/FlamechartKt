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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
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

    Column(
        Modifier
            .padding(16.dp)
            .drawBehind {

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
            color = Color.Blue,
            size = Size(width, heightFactor)
        )
        drawIntoCanvas {
            it.nativeCanvas.apply {
                drawTextLine(
                    line = TextLine.Companion.make(name.substring(0,30), Font(
                        typeface = null,
                        size = 20f,
                    )
                    ),
                    x = startX + 20F,
                    y = startY +25F,
                    paint = Paint()
                )
            }
        }
    }
}

inline val Float.toDp: Dp
@Composable get() = with(LocalDensity.current) { this@toDp.toDp() }