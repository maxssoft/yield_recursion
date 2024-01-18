# Benchmark tests
Benchmark tests for demonstrate the performance of different recursion optimization methods.

These tests show very well the performance problems for the ***yield*** method in secuence.
The standard ***ViewGroup.descendant*** function is built on this solution, and its speed drops hundreds of times due to the use of yield and yieldAll calls.
```
public val ViewGroup.descendants: Sequence<View>
    get() = sequence {
        child.forEach { child ->
            yield(child)
            if (child is ViewGroup) {
                yieldAll(child.descendants)
            }
        }
    }
```

I can offer a much faster solution by using my lazy iterator to traverse trees
https://github.com/maxssoft/yield_recursion/blob/develop/benchmarks/src/main/java/com/maxssoft/func/TreeIterator.kt
```
/**
 * Optimized descendants function based on lazy [TreeIterator]
 */
public val ViewGroup.descendants: Sequence<View>
    get() = treeIteratorOf<View>(this) { view ->
        (view as? ViewGroup)?.children?.iterator()
    }.asSequence()
```


***depth of hierarchy = 1000***
```
main summary:
Benchmark                                Mode  Cnt      Score   Error  Units
RecursionTest.recursionBenchmark        thrpt       45083,831          ops/s
RecursionTest.queueBenchmark            thrpt       14742,257          ops/s
RecursionTest.treeIteratorBsfBenchmark  thrpt       14630,246          ops/s
RecursionTest.treeIteratorDsfBenchmark  thrpt       15337,562          ops/s
RecursionTest.deepRecursiveBenchmark    thrpt       10224,572          ops/s
RecursionTest.yieldBenchmark            thrpt          73,755          ops/s
```

***depth of hierarchy = 3000***
```
main summary:
Benchmark                                Mode  Cnt      Score   Error  Units
RecursionTest.recursionBenchmark        thrpt       16734,099          ops/s
RecursionTest.queueBenchmark            thrpt        5999,774          ops/s
RecursionTest.treeIteratorBsfBenchmark  thrpt        4752,470          ops/s
RecursionTest.treeIteratorDsfBenchmark  thrpt        4973,743          ops/s
RecursionTest.deepRecursiveBenchmark    thrpt        3280,082          ops/s
RecursionTest.yieldBenchmark            thrpt           6,208          ops/s
```

***depth of hierarchy = 5000***
```
main summary:
Benchmark                                Mode  Cnt     Score   Error  Units
RecursionTest.recursionBenchmark         failed (StackOverflow)
RecursionTest.queueBenchmark            thrpt       3245,041          ops/s
RecursionTest.treeIteratorBsfBenchmark  thrpt       2997,227          ops/s
RecursionTest.treeIteratorDsfBenchmark  thrpt       2784,729          ops/s
RecursionTest.deepRecursiveBenchmark    thrpt       1504,135          ops/s
RecursionTest.yieldBenchmark            thrpt          2,430          ops/s
```
test source:  https://github.com/maxssoft/yield_recursion/blob/develop/benchmarks/src/main/java/com/maxssoft/test/RecursionTest.kt
