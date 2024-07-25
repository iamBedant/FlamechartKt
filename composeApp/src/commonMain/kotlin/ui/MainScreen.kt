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
            mapToFlameChartData(intervals)
        )
    })
}
