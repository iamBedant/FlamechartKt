package ui

import androidx.compose.foundation.clickable
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import nav.FlameChartRoot
import processor.TraceNode

@Composable
fun DetailScreen(component: HashMap<Int, List<TraceNode>>, goBack: () -> Unit){
    Text("Detail Screen", modifier = Modifier.clickable { goBack.invoke() })
}