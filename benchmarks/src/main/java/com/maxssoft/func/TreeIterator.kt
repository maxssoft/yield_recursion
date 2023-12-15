package com.maxssoft.func

import com.maxssoft.data.View
import com.maxssoft.data.ViewGroup
import com.maxssoft.data.children

/**
 * Lazy iterator for iterate by abstract hierarchy
 * @param rootIterator Iterator for root elements of hierarchy
 * @param getChildIterator function which get child iterator for current item
 * if current item has child return child iterator else return null
 *
 * Example of using for ViewGroup hierarchy
 * TreeIterator<View>(viewGroup.children.iterator) { (it as? ViewGroup)?.children?.iterator() }
 *
 * @author Сидоров Максим on 15.12.2023
 */
class TreeIterator<T>(
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
            stack.add(iterator)
            iterator = childIterator
        } else {
            while (!iterator.hasNext() && stack.isNotEmpty()) {
                iterator = stack.last()
                stack.removeLast()
            }
        }
    }
}

/**
 * Find views by hierarchy with predicate
 * Demonstrate using [TreeIterator] for optimize recursion
 */
fun ViewGroup.findViewTreeIterator(predicate: (View) -> Boolean): Sequence<View> {
    val treeIterator = TreeIterator<View>(this.children.iterator()) { (it as? ViewGroup)?.children?.iterator() }
    return sequenceOf(this)
        .plus(treeIterator.asSequence())
        .filter { predicate(it) }
}
