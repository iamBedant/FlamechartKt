import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import nav.FlameChartRoot
import nav.FlameChartRootContent
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.DetailScreen
import ui.MainScreen

@Composable
@Preview
fun App(root: FlameChartRootContent) {
    MaterialTheme {
        Children(
            stack = root.childStack,
            animation = stackAnimation(fade() + scale()),
        ) {
            when (val child = it.instance) {
                is FlameChartRoot.Child.Detail -> DetailScreen(child.interval, child.goBack)
                is FlameChartRoot.Child.Main -> MainScreen(child.navigateToDetails)
            }
        }

    }
}
