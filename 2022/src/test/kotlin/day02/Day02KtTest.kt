package day02

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.expect

class Day02KtTest {

    @Test
    fun calculateScore() {
        val input = "A Y\r\n" +
                "B X\r\n" +
                "C Z"
        val result = calculateScore(input)
        assertEquals(15, result)
    }

    @Test
    fun calculateScorePart2() {
        val input = "A Y\r\n" +
                "B X\r\n" +
                "C Z"
        val result = calculateScorePart2(input)
        assertEquals(12, result)
    }
}