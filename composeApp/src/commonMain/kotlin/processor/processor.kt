package processor

fun constructTreeFromStackTraces(traceInterval: TraceInterval): TraceTree {
    val root = TraceTree(
        data = TraceNode(
            functionName = "root",
            firstCaptureTime = traceInterval.startTime,
            lastCaptureTime = traceInterval.endTime,
        ),
        children = mutableListOf()
    )

    traceInterval.traceData.sortedBy { it.captureTime }.forEachIndexed { i, trace ->
        val functionList = trace.stackTrace.split("\n").filter { it.isBlank().not() }.reversed()
        var tempNode: TraceTree = root


        functionList.forEach { functionName ->
            if (tempNode.children.size == 0) {
                tempNode.children.add(
                    TraceTree(
                        data = TraceNode(
                            functionName = functionName,
                            firstCaptureTime = trace.captureTime,
                            lastCaptureTime = trace.captureTime,
                        ),
                        children = mutableListOf()
                    )
                )
                tempNode = tempNode.children.last()
            } else {
                if (tempNode.children.last().data.functionName == functionName) {
                    tempNode.children.last().data =
                        tempNode.children.last().data.copy(lastCaptureTime = trace.captureTime)
                    tempNode = tempNode.children.last()
                } else {
                    tempNode.children.add(
                        TraceTree(
                            data = TraceNode(
                                functionName = functionName,
                                firstCaptureTime = trace.captureTime,
                                lastCaptureTime = trace.captureTime,
                            ),
                            children = mutableListOf()
                        )
                    )
                    tempNode = tempNode.children.last()
                }
            }
        }
    }

    return root.children[0]
}