//import com.goncalossilva.resources.Resource
import kotlinx.serialization.json.Json
import network.IntervalResponse
import processor.TraceData
import processor.TraceInterval

// Relative path is not working for some reason. TODO(" Figure this out ")
val sampleTraceInterval =
    generateTraceIntervalaFromResponse("/Users/iambedant/Documents/Github/FlameChartKt/composeApp/src/commonTest/resources/sampleResponse.json")

fun generateTraceIntervalaFromResponse(filePath: String): TraceInterval {
    TODO()
//    val jsonString = Resource(filePath).readText()
//
//    val response = Json.decodeFromString<IntervalResponse>(jsonString)
//
//    val firstInterval = response.data.intervals[0]
//
//    return TraceInterval(
//        startTime = firstInterval.startTime,
//        endTime = firstInterval.endTime,
//        traceData = firstInterval.anrSampleList.filter { it.threads.size > 0 }.map { sample ->
//            TraceData(
//                captureTime = sample.timestamp,
//                stackTrace = sample.threads[0].lines
//            )
//        }
//    )
}
