package day08

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

class Day08KtTest {

    @Test
    fun part1() {
        val input = File("src/test/kotlin/day08/test08.txt").bufferedReader()
        val result = day08.solve(input)
        assertEquals(21, result)
        val input2 = File("src/main/kotlin/day08/input08.txt").bufferedReader()
        val result2 = day08.solve(input2)
        assertEquals(1789, result2)
    }
}