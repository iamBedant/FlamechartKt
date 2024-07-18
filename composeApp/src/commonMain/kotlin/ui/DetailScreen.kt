package ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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

        LazyColumn() {
            for ((key, value) in component) {
                item {
                    Row {
                        value.forEach {
                            Text(it.functionName)
                            Spacer(Modifier.width(50.dp))
                        }
                    }
                }
            }
        }
    }
}