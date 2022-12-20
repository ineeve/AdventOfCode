package day16

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

class Day16KtTest {

    @Test
    fun parseInput() {
        val file = File("src/test/kotlin/day16/test16.txt")
        val valves = parseInput(file)
        p1(valves)
    }
}