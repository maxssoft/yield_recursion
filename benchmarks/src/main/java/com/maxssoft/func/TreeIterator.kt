package com.maxssoft.func

import com.maxssoft.data.View
import com.maxssoft.data.ViewGroup
import com.maxssoft.data.children
import java.util.LinkedList

/**
 * Create instance of a tree iterator to lazily iterate by hierarchy
 * @param traversalType traversal algorithm type [TraversalType], [TraversalType.BFS] by default
 * @param rootIterator iterator of tree root
 * @param getChildIterator get child iterator for current item of tree
 *      if current item has child return child iterator else return null
 *
 * Example of using for ViewGroup hierarchy
 * TreeIterator<View>(TraversalType.DFS, viewGroup.children.iterator) { (it as? ViewGroup)?.children?.iterator() }
 */
fun <T> treeIteratorOf(
    traversalType: TraversalType = TraversalType.BFS,
    rootIterator: Iterator<T>,
    getChildIterator: ((T) -> Iterator<T>?)
): Iterator<T> =
    when (traversalType) {
        TraversalType.BFS -> TreeIteratorBFS(rootIterator, getChildIterator)
        TraversalType.DFS -> TreeIteratorDFS(rootIterator, getChildIterator)
    }

/**
 * traversal algorithm type
 */
enum class TraversalType {
    /**
     * Depth-first search algorithm (Pre-order)
      */
    DFS,

    /**
     * breadth-first search algorithm
     */
    BFS
}

/**
 * Tree iterator with traversal algorithm DSF [TraversalType.DFS]
 * @param rootIterator iterator of tree root
 * @param getChildIterator get child iterator for current item of tree
 */
private class TreeIteratorDFS<T>(
    rootIterator: Iterator<T>,
    private val getChildIterator: ((T) -> Iterator<T>?)
) : Iterator<T> {
    private val stack = mutableListOf<Iterator<T>>()

    private var iterator: Iterator<T> = rootIterator

    override fun hasNext(): Boolean {
        return iterator.hasNext()
    }

    override fun next(): T {
        val item = iterator.next()
        prepareNextIterator(item)
        return item
    }

    /**
     * calculate next iterator for [item]
     * if current item has child then get child iterator and save current iterator to stack
     * else if current iterator hasn't more elements then restore parent iterator from stack
     */
    private fun prepareNextIterator(item: T) {
        val childIterator = getChildIterator(item)
        if (childIterator != null && childIterator.hasNext()) {
            if (iterator.hasNext()) {
                stack.add(iterator)
            }
            iterator = childIterator
        } else {
            if (!iterator.hasNext() && stack.isNotEmpty()) {
                iterator = stack.last()
                stack.removeLast()
            }
        }
    }
}

/**
 * Tree iterator with traversal algorithm BSF [TraversalType.BFS]
 * @param rootIterator iterator of tree root
 * @param getChildIterator get child iterator for current item of tree
 */
private class TreeIteratorBFS<T>(
    rootIterator: Iterator<T>,
    private val getChildIterator: ((T) -> Iterator<T>?)
) : Iterator<T> {
    private val queue = LinkedList<Iterator<T>>()

    private var iterator: Iterator<T> = rootIterator

    override fun hasNext(): Boolean {
        val hasNext = when {
            iterator.hasNext() -> true
            queue.isNotEmpty() -> {
                iterator = queue.pollFirst()
                true
            }
            else -> false
        }
        return hasNext
    }

    override fun next(): T {
        val item = iterator.next()
        addChildIterator(item)
        return item
    }

    /**
     * add child iterator to queue for current item [item]
     */
    private fun addChildIterator(item: T) {
        val childIterator = getChildIterator(item)
        if (childIterator != null && childIterator.hasNext()) {
            queue.add(childIterator)
        }
    }
}

/**
 * Optimized descendants function based on lazy [TreeIterator]
 * This function works hundreds of times faster than the original function descendants
 */
public val ViewGroup.descendantsTree: Sequence<View>
    get() = treeIteratorOf(rootIterator = children.iterator()) { view ->
        (view as? ViewGroup)?.children?.iterator()
    }.asSequence()

/**
 * Find views by hierarchy with predicate by DSF algorithm
 */
fun ViewGroup.findViewTreeIteratorDSF(predicate: (View) -> Boolean): Sequence<View> {
    return sequenceOf(this)
        .plus(
            treeIteratorOf(traversalType = TraversalType.DFS, rootIterator = children.iterator()) { view ->
                (view as? ViewGroup)?.children?.iterator()
            }.asSequence()
        )
        .filter { predicate(it) }
}

/**
 * Find views by hierarchy with predicate by BSF algorithm
 */
fun ViewGroup.findViewTreeIteratorBSF(predicate: (View) -> Boolean): Sequence<View> {
    return sequenceOf(this)
        .plus(
            treeIteratorOf(traversalType = TraversalType.BFS, rootIterator = children.iterator()) { view ->
                (view as? ViewGroup)?.children?.iterator()
            }.asSequence()
        )
        .filter { predicate(it) }
}
