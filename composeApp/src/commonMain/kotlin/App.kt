import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import network.Interval
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
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
                        AnrInterval(t.value)
                    }

                }
            }
        }
    }
}

@Composable
fun AnrInterval(
    intervals: List<Interval>,
) {
    Text("Interval Hash ${intervals.first().interval_hash}")
}