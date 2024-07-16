import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume
import kotlinx.browser.document
import nav.FlameChartRootContent


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val root = flameChartRootComponent(DefaultComponentContext(lifecycle))
    lifecycle.resume()
    ComposeViewport(document.body!!) {
        App(root)
    }

}

private fun flameChartRootComponent(componentContext: ComponentContext): FlameChartRootContent =
    FlameChartRootContent(
        componentContext = componentContext,
    )