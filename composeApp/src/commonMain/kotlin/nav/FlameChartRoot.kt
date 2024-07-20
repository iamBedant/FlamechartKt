package nav

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import processor.FlameChartData
import processor.TraceNode

interface FlameChartRoot {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class Main(val navigateToDetails: (FlameChartData) -> Unit) : Child()
        data class Detail(val interval: FlameChartData, val goBack: () -> Unit) : Child()
    }
}