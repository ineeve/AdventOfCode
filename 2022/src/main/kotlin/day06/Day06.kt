package day06

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day06/input06.txt").readText()
    solve1(input)
    solve2(input)
}

fun solve1(input: String) {
    var idx = 3
    val queue = ArrayDeque<Char>(4)
    queue.add(input[0])
    queue.addLast(input[1])
    queue.addLast(input[2])
    queue.addLast(input[3])
    val charCount = hashMapOf<Char, Int>()
    for (c in 'a'..'z') {
        charCount.putIfAbsent(c, 0)
    }
    queue.forEach {charCount[it] = charCount[it]!! + 1 }
    while (true) {
        if (charCount.values.all { it < 2 }) break
        val firstChar = queue.removeFirst()
        charCount[firstChar] = charCount[firstChar]!! - 1
        val nextChar = input[idx++]
        queue.addLast(nextChar)
        charCount[nextChar] = charCount[nextChar]!! + 1
    }
    println("Result Part 1: $idx")
}

fun solve2(input: String) {
    var idx = 13
    val queue = ArrayDeque<Char>(14)
    for (c in 0 .. 13) {
        queue.addLast(input[c])
    }
    val charCount = hashMapOf<Char, Int>()
    for (c in 'a'..'z') {
        charCount.putIfAbsent(c, 0)
    }
    queue.forEach {charCount[it] = charCount[it]!! + 1 }
    while (true) {
        if (charCount.values.all { it < 2 }) break
        val firstChar = queue.removeFirst()
        charCount[firstChar] = charCount[firstChar]!! - 1
        val nextChar = input[idx++]
        queue.addLast(nextChar)
        charCount[nextChar] = charCount[nextChar]!! + 1
    }
    println("Result Part 2: $idx")

    val res2 = input.windowed(14).indexOfFirst { it.toCharArray().distinct().size == it.length } + 14
    println("Result Part 2.1: $res2")
}