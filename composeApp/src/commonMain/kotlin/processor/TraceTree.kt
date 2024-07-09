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
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val captureTime: Long
)

data class TraceTree(
    val data: TraceNode,
    val children: MutableList<TraceNode>
)

fun constructTreeFromStackTraces(traceInterval: TraceInterval): TraceTree {
    TODO()
}