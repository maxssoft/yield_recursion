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
    get() = TreeIterator(children.iterator()) { (it as? ViewGroup)?.children?.iterator() }.asSequence()
```


***depth of hierarchy = 1000***
```
main summary:
Benchmark                                Mode  Cnt      Score   Error  Units
RecursionTest.deepRecursiveBenchmark     thrpt       10882,973          ops/s
RecursionTest.recursionBenchmark         thrpt       52938,324          ops/s
RecursionTest.queueBenchmark             thrpt       21272,334          ops/s
RecursionTest.treeIteratorBenchmark      thrpt       10799,548          ops/s
RecursionTest.yieldBenchmark             thrpt          77,978          ops/s
```

***depth of hierarchy = 3000***
```
main summary:
Benchmark                                Mode  Cnt      Score   Error  Units
RecursionTest.deepRecursiveBenchmark     thrpt        3252,411          ops/s
RecursionTest.recursionBenchmark         thrpt       16950,018          ops/s
RecursionTest.queueBenchmark             thrpt        7000,821          ops/s
RecursionTest.treeIteratorBenchmark      thrpt        3554,500          ops/s
RecursionTest.yieldBenchmark             thrpt           6,244          ops/s
```

***depth of hierarchy = 5000***
```
main summary:
Benchmark                                Mode  Cnt     Score   Error  Units
RecursionTest.recursionBenchmark         failed (StackOverflow)
RecursionTest.deepRecursiveBenchmark     thrpt       1877,835          ops/s
RecursionTest.queueBenchmark             thrpt       4928,906          ops/s
RecursionTest.treeIteratorBenchmark      thrpt       2483,408          ops/s
RecursionTest.yieldBenchmark             thrpt          2,240          ops/s
```
test source:  https://github.com/maxssoft/yield_recursion/blob/develop/benchmarks/src/main/java/com/maxssoft/test/RecursionTest.kt
