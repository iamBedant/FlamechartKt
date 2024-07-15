package processor


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
