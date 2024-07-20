package processor

import kotlinx.serialization.Serializable

data class TraceInterval(
    val startTime: Long,
    val endTime: Long,
    val traceData: List<TraceData>
)

data class TraceData(
    val captureTime: Long,
    val stackTrace: String
)

@Serializable
data class TraceNode(
    val functionName: String,
    val firstCaptureTime: Long = 0L,
    val lastCaptureTime: Long = 0L,
)

data class TraceTree(
    var data: TraceNode,
    val children: MutableList<TraceTree>
)

@Serializable
data class FlameChartData(
    val flameMap: HashMap<Int, List<TraceNode>>,
    val firstCaptureTime: Long,
    val lastCaptureTime: Long,
)