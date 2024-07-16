package nav

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import processor.TraceNode

interface FlameChartRoot {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class Main(val navigateToDetails: (HashMap<Int, List<TraceNode>>) -> Unit) : Child()
        data class Detail(val interval: HashMap<Int, List<TraceNode>>, val goBack: () -> Unit) : Child()
    }
}