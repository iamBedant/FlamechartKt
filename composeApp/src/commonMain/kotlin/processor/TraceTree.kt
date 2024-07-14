package processor

data class TraceInterval(
    val startTime: Long,
    val endTime: Long,
    val traceData: List<TraceData>
)

data class TraceData(
    val captureTime: Long,
    val stackTrace: String
)

data class TraceNode(
    val functionName: String,
    val firstCaptureTime: Long = 0L,
    val lastCaptureTime: Long = 0L,
)

data class TraceTree(
    var data: TraceNode,
    val children: MutableList<TraceTree>
)
