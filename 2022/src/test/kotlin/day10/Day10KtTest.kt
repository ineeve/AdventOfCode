package day10

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

class Day10KtTest {

    @Test
    fun part1() {
        val input = File("src/test/kotlin/day10/test10.txt").readText()
        val result = day10.solve(input)
        assertEquals(13140, result)
    }
}