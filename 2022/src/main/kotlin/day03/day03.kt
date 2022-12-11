package day03

import java.io.File

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/day03/input03.txt").readText()
    part1(input)
    part2(input)
}

fun itemPriority(item: Char): Int {
    return if (item.isLowerCase()) {
        item - 'a' + 1
    } else {
        item - 'A' + 27
    }
}

fun part1(input: String): Int {
    val rucksacks = input.split(System.lineSeparator())
    val compartments = rucksacks.map { Pair(it.toCharArray(0,it.length / 2), it.toCharArray(it.length/2)) }
    val commonItems = compartments.map { pair -> pair.first.first { item -> pair.second.contains(item) } }
    val priorities = commonItems.map { itemPriority(it) }
    val result = priorities.sum()
    println("Result 1: $result")
    return result
}

fun part2(input: String): Int {
    val rucksacks = input.split(System.lineSeparator()).map { it.toCharArray().toSet() }.chunked(3)

    val groups = rucksacks.map {
        val groupedMap = hashMapOf<Char, Int>()
        it.map { v ->
            v.fold(groupedMap) { acc, c ->
                acc.putIfAbsent(c, 0)
                acc[c] = acc[c]!!.plus(1)
                acc
            }
        }
        groupedMap.filterValues { v -> v == 3 }.keys.first()
    }
    val score = groups.sumOf { itemPriority(it) }
    println("Result part 2: $score")
    return score
}