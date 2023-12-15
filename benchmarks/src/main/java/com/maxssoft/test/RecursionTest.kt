package com.maxssoft.test

import com.maxssoft.data.View
import com.maxssoft.func.findViewQueue
import com.maxssoft.func.findViewRecursion
import com.maxssoft.func.findViewRecursionOpt
import com.maxssoft.func.findViewTreeIterator
import com.maxssoft.func.findViewYield
import com.maxssoft.test.factory.HierarchyFactory
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Scope
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
@Warmup(iterations = 0)
@Measurement(iterations = 1, time = 20, timeUnit = TimeUnit.MINUTES)
class RecursionTest {

    private val hierarchyFactory = HierarchyFactory()

    val rootView = hierarchyFactory.createHierarchy(5000).also { root ->
        runCatching {
            root.findViewRecursion { it.id % 10 == 0  }.also {
                println("findViewSimple: size = ${it.size}")
            }
        }.getOrElse { println("findViewSimple: failed, Exception: $it") }

        runCatching {
            root.findViewRecursionOpt { it.id % 10 == 0 }.also {
                println("findViewRecursion: size = ${it.size}")
            }
        }.getOrElse { println("findViewRecursion: failed, Exception: $it") }

        runCatching {
            root.findViewQueue { it.id % 10 == 0 }.also {
                println("findViewQueue: size = ${it.size}")
            }
        }.getOrElse { println("findViewQueue: failed, Exception: $it") }

        runCatching {
            root.findViewYield { it.id % 10 == 0 }.also {
                println("findViewYield: size = ${it.count()}")
            }
        }.getOrElse { println("findViewYield: failed, Exception: $it") }

        runCatching {
            root.findViewTreeIterator { it.id % 10 == 0 }.also {
                println("findViewTreeIterator: size = ${it.count()}")
            }
        }.getOrElse { println("findViewTreeIterator: failed, Exception: $it") }
    }

    @Benchmark
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
    fun treeIteratorBenchmark(): List<View> {
        return rootView.findViewTreeIterator { it.id % 10 == 0  }.toList()
    }
}