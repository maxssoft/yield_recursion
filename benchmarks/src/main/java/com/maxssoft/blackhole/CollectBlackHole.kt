package com.maxssoft.blackhole

import kotlinx.benchmark.Blackhole
import java.util.stream.Stream

fun Sequence<*>.collectBlackHole(blackHole: Blackhole) {
    forEach { element->
        blackHole.consume(element)
    }
}

fun Stream<*>.collectBlackHole(blackHole: Blackhole) {
    forEach { element->
        blackHole.consume(element)
    }
}

fun Iterable<*>.collectBlackHole(blackHole: Blackhole) {
    forEach { element->
        blackHole.consume(element)
    }
}

fun Iterator<*>.collectBlackHole(blackHole: Blackhole) {
    forEach { element->
        blackHole.consume(element)
    }
}
