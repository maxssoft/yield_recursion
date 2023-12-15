package com.maxssoft.data

/**
 * Classes for imitate android.View hierarchy for tests depth recursion
 *
 * @author Сидоров Максим on 15.12.2023
 */

interface View {
    val id: Int
}

class SimpleView(override val id: Int): View

class ViewGroup(override val id: Int): View {
    val child = mutableListOf<View>()
}

/**
 * fork code of this function from original package androidx.core.view - ViewGroup.iterator()
 */
public operator fun ViewGroup.iterator(): MutableIterator<View> = object : MutableIterator<View> {
    private var index = 0
    override fun hasNext() = index < child.size
    override fun next() = child[index++] ?: throw IndexOutOfBoundsException()
    override fun remove() {
        child.removeAt(--index)
    }
}

/**
 * fork code of this function from original package androidx.core.view - ViewGroup.children
 */
public val ViewGroup.children: Sequence<View>
    get() = object : Sequence<View> {
        override fun iterator() = this@children.iterator()
    }

/**
 * fork code of this function from original package androidx.core.view - ViewGroup.descendants
 */
public val ViewGroup.descendants: Sequence<View>
    get() = sequence {
        child.forEach { child ->
            yield(child)
            if (child is ViewGroup) {
                yieldAll(child.descendants)
            }
        }
    }

/**
 * fork code of this function from original package androidx.core.view - ViewGroup.descendants
 */
public inline fun ViewGroup.forEach(action: (view: View) -> Unit) {
    for (index in 0 until child.size) {
        action(child[index])
    }
}
