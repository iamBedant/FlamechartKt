package processor

import network.Interval


fun traverseLevelsForTraceTree(tree: TraceTree): HashMap<Int, List<TraceNode>> {
    val map = hashMapOf<Int, List<TraceNode>>()
    var currentLevel = 0
    val currentNode = mutableListOf<TraceTree>()
    currentNode.add(tree)

    while (currentNode.isEmpty().not()) {
        val nextLevelChildren = mutableListOf<TraceTree>()

        for (node in currentNode) {
            nextLevelChildren.addAll(node.children)
        }
        map[currentLevel] = currentNode.map { it.data }
        currentNode.clear()
        currentNode.addAll(nextLevelChildren)
        currentLevel++
    }
    return map
}

fun mapToTraceNode(intervals: List<Interval>): HashMap<Int, List<TraceNode>> {
    val sortedIntervalList = intervals.sortedBy { it.capture_time }
    val firstCaptureTime = sortedIntervalList.first().capture_time
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

fun mapToFlameChartData(intervals: List<Interval>) = FlameChartData(
    flameMap = mapToTraceNode(intervals),
    firstCaptureTime = intervals.sortedBy { it.capture_time }.first().capture_time,
    lastCaptureTime = intervals.sortedBy { it.capture_time }.last().capture_time
)
