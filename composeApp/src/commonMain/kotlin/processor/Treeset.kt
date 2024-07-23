package processor

class TreeSet<T : Comparable<T>> {
    private var root: Node<T>? = null

    private data class Node<T>(var value: T, var left: Node<T>? = null, var right: Node<T>? = null, var height: Int = 1)

    fun add(element: T): Boolean {
        if (contains(element)) return false
        root = add(root, element)
        return true
    }

    private fun add(node: Node<T>?, value: T): Node<T> {
        node ?: return Node(value)
        when {
            value < node.value -> node.left = add(node.left, value)
            value > node.value -> node.right = add(node.right, value)
        }
        return balance(node)
    }

    fun remove(element: T): Boolean {
        if (!contains(element)) return false
        root = remove(root, element)
        return true
    }

    private fun remove(node: Node<T>?, value: T): Node<T>? {
        node ?: return null
        when {
            value < node.value -> node.left = remove(node.left, value)
            value > node.value -> node.right = remove(node.right, value)
            else -> {
                node.left ?: return node.right
                node.right ?: return node.left
                val minNode = findMin(node.right!!)
                node.value = minNode.value
                node.right = remove(node.right, minNode.value)
            }
        }
        return balance(node)
    }

    fun contains(element: T): Boolean {
        return contains(root, element)
    }

    private fun contains(node: Node<T>?, value: T): Boolean {
        node ?: return false
        return when {
            value < node.value -> contains(node.left, value)
            value > node.value -> contains(node.right, value)
            else -> true
        }
    }

    fun getAll(): List<T> {
        val result = mutableListOf<T>()
        inOrderTraversal(root, result)
        return result
    }

    private fun inOrderTraversal(node: Node<T>?, result: MutableList<T>) {
        node ?: return
        inOrderTraversal(node.left, result)
        result.add(node.value)
        inOrderTraversal(node.right, result)
    }

    private fun height(node: Node<T>?): Int {
        return node?.height ?: 0
    }

    private fun balanceFactor(node: Node<T>): Int {
        return height(node.left) - height(node.right)
    }

    private fun balance(node: Node<T>): Node<T> {
        updateHeight(node)
        return when {
            balanceFactor(node) > 1 -> {
                if (balanceFactor(node.left!!) < 0) {
                    node.left = rotateLeft(node.left!!)
                }
                rotateRight(node)
            }
            balanceFactor(node) < -1 -> {
                if (balanceFactor(node.right!!) > 0) {
                    node.right = rotateRight(node.right!!)
                }
                rotateLeft(node)
            }
            else -> node
        }
    }

    private fun rotateLeft(node: Node<T>): Node<T> {
        val rightNode = node.right!!
        node.right = rightNode.left
        rightNode.left = node
        updateHeight(node)
        updateHeight(rightNode)
        return rightNode
    }

    private fun rotateRight(node: Node<T>): Node<T> {
        val leftNode = node.left!!
        node.left = leftNode.right
        leftNode.right = node
        updateHeight(node)
        updateHeight(leftNode)
        return leftNode
    }

    private fun updateHeight(node: Node<T>) {
        node.height = 1 + maxOf(height(node.left), height(node.right))
    }

    private fun findMin(node: Node<T>): Node<T> {
        return node.left?.let { findMin(it) } ?: node
    }

    private fun findMax(node: Node<T>): Node<T> {
        return node.right?.let { findMax(it) } ?: node
    }

    fun lower(element: T): T? {
        var node = root
        var result: Node<T>? = null

        while (node != null) {
            if (element > node.value) {
                result = node
                node = node.right
            } else {
                node = node.left
            }
        }
        return result?.value
    }

    fun higher(element: T): T? {
        var node = root
        var result: Node<T>? = null

        while (node != null) {
            if (element < node.value) {
                result = node
                node = node.left
            } else {
                node = node.right
            }
        }
        return result?.value
    }
}