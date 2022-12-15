package day13

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File

class Day13KtTest {

    @Test
    fun solve() {
        val input = File("src/test/kotlin/day13/test13.txt").readText()
        val p1 = solve(input)
        kotlin.test.assertEquals(13, p1)
    }
}