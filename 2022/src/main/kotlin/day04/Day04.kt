package day04

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day04/input04.txt").readText()
    solve(input)
}

fun sectionRange(str: String): Pair<Int, Int> {
    val values = str.split("-")
    return Pair(values[0].toInt(), values[1].toInt())
}

fun anyOverlap(elf1: Pair<Int, Int>, elf2: Pair<Int, Int>): Boolean {
    return elf1.first <= elf2.first && elf1.second >= elf2.first
            || elf1.first <= elf2.first && elf1.second >= elf2.second
}

fun solve(input: String): Int {
    val allSections = input.split(System.lineSeparator()).map {
        val elfPair = it.split(",")
        val elf1 = sectionRange(elfPair[0])
        val elf2 = sectionRange(elfPair[1])
        Pair(elf1, elf2)
    }
    val fullOverlappingSections = allSections.filter { (elf1, elf2) -> (
            elf1.first <= elf2.first && elf1.second >= elf2.second)
            || (elf2.first <= elf1.first && elf2.second >= elf1.second)
    }

    val anyOverlapSections = allSections.filter { anyOverlap(it.first, it.second) || anyOverlap(it.second, it.first) }
    val result1 = fullOverlappingSections.count()
    println("Result1: $result1")
    val result2 = anyOverlapSections.count()
    println("Result2: $result2")
    return result1
}