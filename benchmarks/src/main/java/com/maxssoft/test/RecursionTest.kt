package com.maxssoft.test

import com.maxssoft.data.View
import com.maxssoft.data.ViewGroup
import com.maxssoft.func.findViewDeepRecursive
import com.maxssoft.func.findViewQueue
import com.maxssoft.func.findViewRecursion
import com.maxssoft.func.findViewRecursionOpt
import com.maxssoft.func.findViewTreeIteratorBSF
import com.maxssoft.func.findViewTreeIteratorDSF
import com.maxssoft.func.findViewYield
import com.maxssoft.test.factory.HierarchyFactory
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.benchmark.Warmup
import org.openjdk.jmh.annotations.Fork
import java.util.concurrent.TimeUnit

/**
 * Tests for demonstrate performance of different kinds of recursion
 *
 * @author Сидоров Максим on 10.12.2023
 */
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 25)
class RecursionTest {

    private lateinit var rootView: ViewGroup
    private val DEBUG: Boolean = false

    @Setup
    fun setup() {
        rootView = HierarchyFactory().createHierarchy(5000)
        if (DEBUG) {
            runCatching {
                rootView.findViewRecursion { it.id % 10 == 0 }.also {
                    println("findViewSimple: size = ${it.size}")
                }
            }.getOrElse { println("findViewSimple: failed, Exception: $it") }

            runCatching {
                rootView.findViewRecursionOpt { it.id % 10 == 0 }.also {
                    println("findViewRecursion: size = ${it.size}")
                }
            }.getOrElse { println("findViewRecursion: failed, Exception: $it") }

            runCatching {
                rootView.findViewQueue { it.id % 10 == 0 }.also {
                    println("findViewQueue: size = ${it.size}")
                }
            }.getOrElse { println("findViewQueue: failed, Exception: $it") }

            runCatching {
                rootView.findViewYield { it.id % 10 == 0 }.also {
                    println("findViewYield: size = ${it.count()}")
                }
            }.getOrElse { println("findViewYield: failed, Exception: $it") }

            runCatching {
                rootView.findViewTreeIteratorDSF { it.id % 10 == 0 }.also {
                    println("findViewTreeIteratorDSF: size = ${it.count()}")
                }
            }.getOrElse { println("findViewTreeIteratorDSF: failed, Exception: $it") }

            runCatching {
                rootView.findViewTreeIteratorBSF { it.id % 10 == 0 }.also {
                    println("findViewTreeIteratorBSF: size = ${it.count()}")
                }
            }.getOrElse { println("findViewTreeIteratorBSF: failed, Exception: $it") }

            runCatching {
                rootView.findViewDeepRecursive { it.id % 10 == 0 }.also {
                    println("findViewDeepRecursive: size = ${it.count()}")
                }
            }.getOrElse { println("findViewDeepRecursive: failed, Exception: $it") }

        }
    }

    // @Benchmark()
    fun simpleBenchmark(): List<View> {
        return rootView.findViewRecursion { it.id % 10 == 0  }
    }

    @Benchmark
    fun recursionBenchmark(): List<View> {
        return rootView.findViewRecursionOpt { it.id % 10 == 0  }
    }

    @Benchmark
    fun queueBenchmark(): List<View> {
        return rootView.findViewQueue { it.id % 10 == 0  }
    }

    @Benchmark
    fun yieldBenchmark(): List<View> {
        return rootView.findViewYield { it.id % 10 == 0  }.toList()
    }

    @Benchmark
    fun treeIteratorDsfBenchmark(): List<View> {
        return rootView.findViewTreeIteratorDSF { it.id % 10 == 0  }.toList()
    }

    @Benchmark
    fun treeIteratorBsfBenchmark(): List<View> {
        return rootView.findViewTreeIteratorBSF { it.id % 10 == 0  }.toList()
    }

    @Benchmark
    fun deepRecursiveBenchmark(): List<View> {
        return rootView.findViewDeepRecursive { it.id % 10 == 0  }
    }
}