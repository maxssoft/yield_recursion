package com.maxssoft.func

import com.maxssoft.data.View
import com.maxssoft.data.ViewGroup
import com.maxssoft.data.children

/**
 * Find views by hierarchy with predicate
 * Demonstrate recursion optimization with [DeepRecursiveFunction]
 */
fun ViewGroup.findViewDeepRecursive(predicate: (View) -> Boolean): List<View> {
    val result = mutableListOf<View>()
    val recursion = DeepRecursiveFunction<View, List<View>> { view ->
        if (predicate(view)) {
            result.add(view)
        }
        if (view is ViewGroup) {
            view.children.forEach {
                callRecursive(it)
            }
        }

        result
    }
    return recursion(this)
}