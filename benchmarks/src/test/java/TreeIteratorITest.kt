import com.maxssoft.data.ViewGroup
import com.maxssoft.data.children
import com.maxssoft.func.TraversalType
import com.maxssoft.func.treeIteratorOf
import com.maxssoft.test.factory.HierarchyFactory
import org.junit.Assert
import org.junit.Test

/**
 * Tests for demonstrate performance of different kinds of recursion
 *
 * @author Сидоров Максим on 10.12.2023
 */
class TreeIteratorITest {

    private val hierarchyFactory = HierarchyFactory()

    private val rootView = hierarchyFactory.createTestTree()

    @Test
    fun dsfTest() {
        val expected = "B,D,E,G,H,J,C,F,I,K,M,L,"
        val sequence = treeIteratorOf(
            traversalType = TraversalType.DFS,
            rootIterator = rootView.children.iterator()
        ) { view ->
            (view as? ViewGroup)?.children?.iterator()
        }.asSequence()
        val actual = sequence.fold("") { value, view -> value + "${view.name}," }
        println("DSF Test -------------------------------")
        println("expected = $expected")
        println("actual   = $actual")
        println("")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun bsfTest() {
        val expected = "B,C,D,E,F,G,H,I,J,K,L,M,"
        val sequence = treeIteratorOf(
            traversalType = TraversalType.BFS,
            rootIterator = rootView.children.iterator()
        ) { view ->
            (view as? ViewGroup)?.children?.iterator()
        }.asSequence()
        val actual = sequence.fold("") { value, view -> value + "${view.name}," }
        println("BSF Test -------------------------------")
        println("expected = $expected")
        println("actual   = $actual")
        println("")
        Assert.assertEquals(expected, actual)
    }
}