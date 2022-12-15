package day14

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

class Day14KtTest {

    @Test
    fun solveP1() {
        val input = File("src/test/kotlin/day14/test14.txt")
        val p1 = solveP1(input)
        assertEquals(24,p1)
    }

    @Test
    fun solveP2() {
        val input = File("src/test/kotlin/day14/test14.txt")
        val p2 = solveP2(input)
        assertEquals(93,p2)
    }
}