package com.maxssoft.test

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.benchmark.Warmup
import org.openjdk.jmh.annotations.Fork
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 0)
@Measurement(iterations = 1, time = 20, timeUnit = TimeUnit.MINUTES)
class YieldTest {

    private lateinit var list: List<Int>

    @Setup
    fun setup() {
        list = (0..1000).toList()
    }

    @Benchmark
    fun iteratorBenchmark(): List<Int> {
        var result = mutableListOf<Int>()
        val iterator = sequence {
            list.forEach { yield(it) }
        }.iterator()

        iterator.forEach { result.add(it) }
        return result
    }

    @Benchmark
    fun yieldBenchmark(): List<Int> {
        var result = mutableListOf<Int>()
        val iterator = list.iterator()

        iterator.forEach { result.add(it) }
        return result
    }
}