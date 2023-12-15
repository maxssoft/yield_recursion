package com.maxssoft.func

import com.maxssoft.data.View
import com.maxssoft.data.ViewGroup
import com.maxssoft.data.descendants

/**
 * Find views by hierarchy with predicate
 * Demonstrate recursion optimization with using a yield and sequence
 */
fun ViewGroup.findViewYield(predicate: (View) -> Boolean): Sequence<View> {
    return sequenceOf(this)
        .plus(this.descendants)
        .filter { predicate(it) }
}
