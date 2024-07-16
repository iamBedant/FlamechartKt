package nav

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import processor.TraceNode


class FlameChartRootContent(
    componentContext: ComponentContext,
) : FlameChartRoot, ComponentContext by componentContext {
    private val navigation = StackNavigation<Configuration>()

    private val stack = childStack(
        source = navigation,
        initialConfiguration = Configuration.Main(::navigateToDetails),
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = Configuration.serializer()
    )

    override val childStack: Value<ChildStack<*, FlameChartRoot.Child>>
        get() = stack

    private fun createChild(configuration: Configuration, componentContext: ComponentContext): FlameChartRoot.Child =
        when (configuration) {
            is Configuration.Main -> FlameChartRoot.Child.Main(::navigateToDetails)
            is Configuration.Detail -> FlameChartRoot.Child.Detail(configuration.interval, ::goBack)
        }

    private fun navigateToDetails(stackMap: HashMap<Int, List<TraceNode>>){
        navigation.push(Configuration.Detail(stackMap, ::goBack))
    }
    private fun goBack() {
        navigation.pop()
    }
    
    @Serializable
    private sealed class Configuration {
        @Serializable
        data class Main(val navigateToDetails: (stackMap: HashMap<Int, List<TraceNode>>) -> Unit) : Configuration()

        @Serializable
        data class Detail(val interval: HashMap<Int, List<TraceNode>>, val goBack: () -> Unit) : Configuration()
    }
}