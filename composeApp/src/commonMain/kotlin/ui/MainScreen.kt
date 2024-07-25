package ui

import NetworkClient
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.serialization.json.Json
import network.Interval
import network.IntervalResponse
import processor.*

@Composable
fun MainScreen(navigatrToDetails: (FlameChartData) -> Unit) {
    var response by remember { mutableStateOf((mapOf<String, List<Interval>>())) }

    LaunchedEffect(true) {
        val intervalLogs = mutableMapOf<String, List<Interval>>()

        val responseSt = Json.decodeFromString<IntervalResponse>(sampleTrace)
        responseSt.data.intervals.forEach { interval ->
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


//        NetworkClient().getIntervals().data.intervals.forEach { interval ->
//            val hash = interval.anrHash
//            val anrList = mutableListOf<Interval>()
//
//            interval.anrSampleList.forEach { sample ->
//                if (sample.threads.size > 0) {
//                    anrList.add(
//                        Interval(
//                            hash,
//                            interval.startTime,
//                            interval.endTime,
//                            sample.timestamp,
//                            sample.threads[0].name,
//                            sample.threads[0].lines
//                        )
//                    )
//                }
//            }
//
//            intervalLogs[hash] = anrList
//
//        }
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
    navigatrToDetails: (FlameChartData) -> Unit,
) {
    Text("Interval Hash ${intervals.first().interval_hash}  == ${intervals.size}", modifier = Modifier.clickable {
        navigatrToDetails.invoke(
            FlameChartData(
                flameMap = mapToTraceNode(intervals),
                firstCaptureTime = intervals.sortedBy { it.capture_time }.first().capture_time,
                lastCaptureTime = intervals.sortedBy { it.capture_time }.last().capture_time
            )
        )
    })
}

fun mapToTraceNode(intervals: List<Interval>): HashMap<Int, List<TraceNode>> {
    val sortedIntervalList = intervals.sortedBy { it.capture_time }
    val firstCaptureTime = sortedIntervalList.first().capture_time
    val lastCaptureTime = sortedIntervalList.last().capture_time

    val traceInterval = TraceInterval(
        startTime = intervals[0].start_time - firstCaptureTime,
        endTime = intervals[0].end_time - firstCaptureTime,
        traceData = intervals.map {
            TraceData(
                captureTime = it.capture_time - firstCaptureTime,
                stackTrace = it.stack_trace
            )
        }
    )
    val traceTree = constructTreeFromStackTraces(traceInterval)
    val uiMap = traverseLevelsForTraceTree(traceTree)
    val captureTimeSet: TreeSet<Long> = TreeSet()
    intervals.forEach {
        captureTimeSet.add(it.capture_time - firstCaptureTime)
    }

    val uiPaddedMap = addPadding(uiMap, captureTimeSet)
    return uiPaddedMap
}

fun addPadding(
    uiMap: HashMap<Int, List<TraceNode>>,
    captureTimeSet: TreeSet<Long>,
): HashMap<Int, List<TraceNode>> {
    val map = hashMapOf<Int, List<TraceNode>>()
    for ((key, value) in uiMap) {
        val paddedList = addPaddingToList(value, captureTimeSet)
        map[key] = paddedList
    }
    return map
}

fun addPaddingToList(value: List<TraceNode>, captureTimeSet: TreeSet<Long>): List<TraceNode> {
    val traceNodeList = mutableListOf<TraceNode>()
    for (i in value.indices) {
        var traceNode = value[i]
        traceNode = traceNode.copy(
            firstDrawBoundary = calculateLeftPadding(traceNode, captureTimeSet),
            lastDrawBoundary = calculateRightPadding(traceNode, captureTimeSet)
        )
        println(" ${traceNode.firstDrawBoundary}, {${traceNode.lastDrawBoundary}}")
        println(" ${traceNode.firstCaptureTime}, {${traceNode.lastCaptureTime}}")
        println(" ====================")
        traceNodeList.add(traceNode)
    }

    return traceNodeList
}

fun calculateRightPadding(traceNode: TraceNode, captureTimeSet: TreeSet<Long>): Long {
    val rightGap = captureTimeSet.higher(traceNode.lastCaptureTime)?.let {
        it - traceNode.lastCaptureTime
    } ?: 0

    println("Right Gap $rightGap")
    println("LastCaptureTimeOfCurrenNode Gap ${traceNode.lastCaptureTime}")
    val rightPadding = rightGap / 2
    return traceNode.lastCaptureTime + rightPadding
}

fun calculateLeftPadding(traceNode: TraceNode, captureTimeSet: TreeSet<Long>): Long {
    val leftGap = captureTimeSet.lower(traceNode.firstCaptureTime)?.let {
        traceNode.firstCaptureTime - it
    } ?: 0
    val leftPadding = leftGap / 2
    return traceNode.firstCaptureTime - leftPadding
}

