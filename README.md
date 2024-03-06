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

results for Jetbrains Runtime 17, M1 Pro
recursion call stackOverflow on 7.000 depth of hierarchy

***depth of hierarchy = 1000***
```
main summary:
Benchmark                               Mode  Cnt         Score        Error  Units
RecursionTest.recursionBenchmark        avgt   25     17492,470 ±     95,221  ns/op
RecursionTest.queueBenchmark            avgt   25     54254,740 ±   2140,701  ns/op
RecursionTest.treeIteratorBsfBenchmark  avgt   25     62299,076 ±    539,457  ns/op
RecursionTest.treeIteratorDsfBenchmark  avgt   25     33261,682 ±    218,576  ns/op
RecursionTest.deepRecursiveBenchmark    avgt   25     70048,183 ±   2696,998  ns/op
RecursionTest.yieldBenchmark            avgt   25  16806854,237 ± 308983,896  ns/op
```

***depth of hierarchy = 3000***
```
main summary:
Benchmark                               Mode  Cnt          Score         Error  Units
RecursionTest.recursionBenchmark        avgt   25      52453,463 ±     878,258  ns/op
RecursionTest.queueBenchmark            avgt   25     152197,084 ±    6738,351  ns/op
RecursionTest.treeIteratorBsfBenchmark  avgt   25     185949,626 ±    3678,994  ns/op
RecursionTest.treeIteratorDsfBenchmark  avgt   25      96104,052 ±    1017,141  ns/op
RecursionTest.deepRecursiveBenchmark    avgt   25     173037,527 ±    1951,805  ns/op
RecursionTest.yieldBenchmark            avgt   25  195829303,038 ± 3157609,640  ns/op
```

***depth of hierarchy = 5000***
```
main summary:
Benchmark                               Mode  Cnt          Score          Error  Units
RecursionTest.recursionBenchmark        avgt   25      85964,743 ±      360,862  ns/op
RecursionTest.queueBenchmark            avgt   25     246697,554 ±     1542,717  ns/op
RecursionTest.treeIteratorBsfBenchmark  avgt   25     343424,228 ±     4143,974  ns/op
RecursionTest.treeIteratorDsfBenchmark  avgt   25     170995,500 ±     3044,422  ns/op
RecursionTest.deepRecursiveBenchmark    avgt   25     281738,594 ±     2473,126  ns/op
RecursionTest.yieldBenchmark            avgt   25  558457428,666 ± 60149337,508  ns/op
```
test source:  https://github.com/maxssoft/yield_recursion/blob/develop/benchmarks/src/main/java/com/maxssoft/test/RecursionTest.kt
