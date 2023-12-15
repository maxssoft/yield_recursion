package com.maxssoft.func

import com.maxssoft.data.View
import com.maxssoft.data.ViewGroup
import com.maxssoft.data.children

/**
 * functions of find views with using recursion
 *
 * @author Сидоров Максим on 15.12.2023
 */

/**
 * Find views by hierarchy with predicate
 * Simple recursion function with copy arrays in recursion
 */
fun ViewGroup.findViewRecursion(predicate: (View) -> Boolean): List<View> {
    val accumulator = mutableListOf<View>()
    if (predicate(this)) {
        accumulator.add(this)
    }
    children.forEach { child ->
        when {
            child is ViewGroup -> accumulator.addAll(child.findViewRecursion(predicate))
            predicate(child) -> accumulator.add(child)
        }
    }
    return accumulator
}

/**
 * Find views by hierarchy with predicate
 * Optimized recursion function without copy arrays in recursion
 * copy arrays replace to accumulate in mutable list
 */
fun ViewGroup.findViewRecursionOpt(predicate: (View) -> Boolean): List<View> {
    val accumulator = mutableListOf<View>()
    this.internalFindView(predicate, accumulator)
    return accumulator
}

private fun ViewGroup.internalFindView(
    predicate: (View) -> Boolean,
    accumulator: MutableList<View> = mutableListOf()
) {
    if (predicate(this)) {
        accumulator.add(this)
    }
    children.forEach { child ->
        when {
            child is ViewGroup -> child.internalFindView(predicate, accumulator)
            predicate(child) -> accumulator.add(child)
        }
    }
}
