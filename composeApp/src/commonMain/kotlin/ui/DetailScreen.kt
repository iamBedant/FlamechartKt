package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nav.FlameChartRoot
import processor.TraceNode

@Composable
fun DetailScreen(component: HashMap<Int, List<TraceNode>>, goBack: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("GoBack", modifier = Modifier.clickable { goBack.invoke() })
        Spacer(Modifier.height(60.dp))

        val stateVertical = rememberScrollState(0)
        val stateHorizontal = rememberScrollState(0)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateVertical)
                .padding(end = 12.dp, bottom = 12.dp)
                .horizontalScroll(stateHorizontal)
        ){
            Column {
                for ((key, value) in component) {
                    Row {
                        value.forEach {
                            Text(it.functionName)
                            Spacer(Modifier.width(50.dp))
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}