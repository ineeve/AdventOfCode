package day11

import org.junit.jupiter.api.Test

import java.io.File

class Day11KtTest {

    @Test
    fun solve() {
        val input =  File("src/test/kotlin/day11/test11.txt").readText()
        val monkeys = input.split("\r\n\r\n").map { Monkey.fromInput(it) }
        day11.solveP2(monkeys, 10000)
    }
}