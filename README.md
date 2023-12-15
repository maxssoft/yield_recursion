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
Benchmark                             Mode  Cnt      Score   Error  Units
RecursionTest.recursionBenchmark     thrpt       31536,079          ops/s
RecursionTest.queueBenchmark         thrpt       10242,561          ops/s
RecursionTest.treeIteratorBenchmark  thrpt        6982,174          ops/s
RecursionTest.yieldBenchmark         thrpt          61,194          ops/s
```

***depth of hierarchy = 3000***
```
main summary:
Benchmark                             Mode  Cnt      Score   Error  Units
RecursionTest.recursionBenchmark     thrpt       14018,770          ops/s
RecursionTest.queueBenchmark         thrpt        6030,651          ops/s
RecursionTest.treeIteratorBenchmark  thrpt        3182,863          ops/s
RecursionTest.yieldBenchmark         thrpt           6,055          ops/s
```

***depth of hierarchy = 5000***
```
main summary:
Benchmark                             Mode  Cnt     Score   Error  Units
RecursionTest.recursionBenchmark     failed (StackOverflow)
RecursionTest.queueBenchmark         thrpt       1961,384          ops/s
RecursionTest.treeIteratorBenchmark  thrpt       1310,270          ops/s
RecursionTest.yieldBenchmark         thrpt          1,826          ops/s
```
test source:  https://github.com/maxssoft/yield_recursion/blob/develop/benchmarks/src/main/java/com/maxssoft/test/RecursionTest.kt
