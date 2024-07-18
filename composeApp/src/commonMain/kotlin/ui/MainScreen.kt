package ui

import NetworkClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import network.Interval
import processor.*

@Composable
fun MainScreen(navigatrToDetails: (HashMap<Int, List<TraceNode>>) -> Unit) {
    var response by remember { mutableStateOf((mapOf<String, List<Interval>>())) }

    LaunchedEffect(true) {
        val intervalLogs = mutableMapOf<String, List<Interval>>()

        NetworkClient().getIntervals().data.intervals.forEach { interval ->
            val hash = interval.anrHash
            val anrList = mutableListOf<Interval>()

            interval.anrSampleList.forEach { sample ->
                if (sample.threads.size > 0) {
                    anrList.add(
                        Interval(
                            hash,
                            interval.startTime,
                            interval.endTime,
                            sample.timestamp,
                            sample.threads[0].name,
                            sample.threads[0].lines
                        )
                    )
                }
            }

            intervalLogs[hash] = anrList

        }
        response = intervalLogs
    }
    Column {
        Text("This is the starting of this website")
        LazyColumn() {
            response.forEach { t ->
                item(t.key) {
                    AnrInterval(t.value, navigatrToDetails)
                }
            }
        }
    }
}

@Composable
fun AnrInterval(
    intervals: List<Interval>,
    navigatrToDetails: (HashMap<Int, List<TraceNode>>) -> Unit,
) {
    Text("Interval Hash ${intervals.first().interval_hash}", modifier = Modifier.clickable {
        navigatrToDetails.invoke(
            mapToTraceNode(intervals)
        )
    })
}

fun mapToTraceNode(intervals: List<Interval>): HashMap<Int, List<TraceNode>> {
    val traceInterval = TraceInterval(
        startTime = intervals[0].start_time,
        endTime = intervals[0].end_time,
        traceData = intervals.map {
            TraceData(
                captureTime = it.capture_time,
                stackTrace = it.stack_trace
            )
        }
    )
    val traceTree = constructTreeFromStackTraces(traceInterval)
    val uiMap = traverseLevelsForTraceTree(traceTree)
    return uiMap
}
