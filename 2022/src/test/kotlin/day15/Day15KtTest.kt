package day15

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

class Day15KtTest {

    @Test
    fun solveP1() {
        val file = File("src/test/kotlin/day15/test15.txt")
        val res = day15.solveP1(file, 10)
        assertEquals(26, res)
    }
}