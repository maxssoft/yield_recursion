package com.maxssoft.test

import com.maxssoft.blackhole.collectBlackHole
import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Scope
import kotlinx.benchmark.Setup
import kotlinx.benchmark.State
import kotlinx.benchmark.Warmup
import org.openjdk.jmh.annotations.Fork
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 25)
class YieldTest {

    private lateinit var list: List<Int>

    @Setup
    fun setup() {
        list = (0..1000).toList()
    }

    @Benchmark
    fun iteratorBenchmark(blackHole: Blackhole) {
        sequence {
            list.forEach { yield(it) }
        }.collectBlackHole(blackHole)
    }

    @Benchmark
    fun yieldBenchmark(blackHole: Blackhole) {
        list.iterator().collectBlackHole(blackHole)
    }
}