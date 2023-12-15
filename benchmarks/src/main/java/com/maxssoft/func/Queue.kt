package com.maxssoft.func

import com.maxssoft.data.View
import com.maxssoft.data.ViewGroup
import com.maxssoft.data.children
import java.util.LinkedList
import java.util.Queue

/**
 * Find views by hierarchy with predicate
 * Demonstrate recursion optimization with using a queue
 */
fun ViewGroup.findViewQueue(predicate: (View) -> Boolean): List<View> {
    val accumulator = mutableListOf<View>()
    val queue: Queue<View> = LinkedList()
    queue.add(this) // add self as first element of queue
    while (queue.isNotEmpty()) {
        val view = queue.poll() // get and remove next item from queue
        if (predicate(view)) {
            accumulator.add(view)
        }
        if (view is ViewGroup) { // add to Queue all child items for current view
            view.children.forEach { queue.add(it) }
        }
    }

    return accumulator
}
