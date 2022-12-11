package day01

import java.io.File


fun main(args: Array<String>) {
    val lines = File("src/main/kotlin/day01/input01.txt").readText()
    val calories = lines.split("\r\n\r\n").map { it.split(System.lineSeparator()).map { v -> v.toInt() } }
    val sums = calories.map { it.sum() }.sorted().reversed()
    val result1 = sums[0]
    val result2 = sums[0] + sums[1] + sums[2]
    println("Result 1: $result1")
    println("Result 2: $result2")
}