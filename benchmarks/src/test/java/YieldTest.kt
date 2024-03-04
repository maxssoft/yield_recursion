import org.junit.Test

/**
 *
 *
 * @author Сидоров Максим on 03.02.2024
 */
class YieldTest {

    @Test
    fun test() {
        val iterator = IteratorWrapper(createYieldData().iterator())
        while (iterator.hasNext()) {
            val item = iterator.next()
            println("item: $item")
        }
    }

    private fun createYieldData(): Sequence<Int> {
        return sequence {
            println("call yield(0), nextValue = 0")
            println("suspend ---------")
            yield(0)
            // ----- suspending -------

            println("call yieldAll(1,2,3)")
            println("nextIterator = iterator(1,2,3)")
            println("suspend----------")
            yieldAll(listOf(1,2,3))
            // ----- suspending -------

            println("start for cycle")
            for (i in 4..8) {
                println("call yield($i), nextValue = $i")
                println("suspend----------")
                yield(i)
                // ----- suspending -------
            }
        }
    }

    private suspend fun mySuspendFunc(value: Int) {
        println(value)
    }

    private fun createYieldData1(): Sequence<Int> {
        return sequence {
            println("start for cycle")
            for (i in 4..8) {
                println("call yield($i), nextValue = $i")
                println("suspend----------")

                //mySuspendFunc(0)

                yield(i)
                // ----- suspending -------
            }
        }
    }

    private fun createYieldData2(): Sequence<String> {
        println("createData()")
        return sequence {
            println("start scope")

            println("call yield 0")
            yield("0")

            println("call yield 1")
            yield("2")

            println("call yield 3")
            yield("2")

            println("call yield 4")
            yield("5")

            println("call yield 6")
            yield("6")

            println("finish scope")
        }
    }


    class IteratorWrapper<T>(private val iterator: Iterator<T>): Iterator<T> {
        override fun hasNext(): Boolean {
            print("iterator.hasNext() -> ")
            return iterator.hasNext()
        }

        override fun next(): T {
            print("iterator.next() -> ")
            return iterator.next()
        }
    }
}