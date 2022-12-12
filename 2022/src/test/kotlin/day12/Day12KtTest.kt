package day12

import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day12KtTest {

    @Test
    fun part1() {
        val input = File("src/test/kotlin/day12/test12.txt").readText()
        val p1 = day12.part1(input)
        assertEquals(31, p1)
    }

    @Test
    fun part2() {
        val input = File("src/test/kotlin/day12/test12.txt").readText()
        val p2 = day12.part2(input)
        assertEquals(29, p2)
    }
}