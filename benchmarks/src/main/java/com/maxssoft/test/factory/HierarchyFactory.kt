package com.maxssoft.test.factory

import com.maxssoft.data.SimpleView
import com.maxssoft.data.ViewGroup
import com.maxssoft.data.children

/**
 * Create test hierarchy of view with specified depth
 *
 * Example of hierarchy
 * view, viewGroup, view
 *          | view, viewGroup, view
 *                      | view, viewGroup, view
 *
 * @author Сидоров Максим on 15.12.2023
 */
class HierarchyFactory {

    /**
     * Create test hierarchy of View with specified [depth]
     *
     * Create hierarchy by 10 parts because depth may be large value and may get StackOverflowException
     */
    fun createHierarchy(depth: Int): ViewGroup {
        viewCounter = 0
        val countParts = 10
        val partDepth = depth / countParts

        val root = ViewGroup(newId())
        root.createViews(0, partDepth) {
            child.add(SimpleView(newId()))
            child.add(ViewGroup(newId()))
            child.add(SimpleView(newId()))
        }
        for (i in 1 until 10) {
            root.findDepthGroup().createViews(0, partDepth) {
                child.add(SimpleView(newId()))
                child.add(ViewGroup(newId()))
                child.add(SimpleView(newId()))
            }
        }
        return root
    }

    private var viewCounter: Int = 0
    private fun newId(): Int = viewCounter++

    /**
     * looks for a max deep ViewGroup in the hierarchy
     */
    private tailrec fun ViewGroup.findDepthGroup(): ViewGroup {
        return if (this.child.isEmpty()) {
            this
        }
        else {
            this.child.filterIsInstance<ViewGroup>().first().findDepthGroup()
        }
    }

    /**
     * Create hierarchy of views with specified [depth]
     * Each level of hierarchy created by call [viewFactory] function
     */
    private fun ViewGroup.createViews(depth: Int, maxDepth: Int, viewFactory: ViewGroup.() -> Unit) {
        if (depth < maxDepth) {
            this.viewFactory()
            this.children
                .filterIsInstance<ViewGroup>()
                .forEach { it.createViews(depth + 1, maxDepth, viewFactory) }
        }
    }

}