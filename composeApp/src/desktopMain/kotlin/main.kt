import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import nav.FlameChartRootContent

fun main() = application {
    val lifecycle = LifecycleRegistry()
    val root = remember { flameChartRootComponent(DefaultComponentContext(lifecycle))}
    Window(
        onCloseRequest = ::exitApplication,
        title = "FlameChartKt",
    ) {
        App(root)
    }
}

private fun flameChartRootComponent(componentContext: ComponentContext): FlameChartRootContent =
    FlameChartRootContent(
        componentContext = componentContext,
    )