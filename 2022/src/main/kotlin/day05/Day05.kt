package day05

import java.io.File
import java.util.*

data class Move(var quantity: Int, val from: Int, val to: Int)

fun main() {
    val input = File("src/main/kotlin/day05/input05.txt").readText()
    solve1(input)
    solve2(input)
}

fun parseInputStacks(stacksStr: String): List<Stack<String>> {
    val lines = stacksStr.split(System.lineSeparator())
    val stacks = List(9) {Stack<String>()}
    lines.reversed().map { it.split("\\s{1,4}".toRegex())
        .map { crate -> crate.trim() }
        .forEachIndexed{ idx, crate ->
            if (crate.isNotEmpty()) {
                stacks[idx].push(crate)
            }
        }
    }

    return stacks
}

fun solve1(input: String) {
    val stacksEndIdx = input.indexOfFirst { it == '1' }
    val stacksStr = input.substring(0, stacksEndIdx - 3)
    val movesStr = input.substring(input.indexOfFirst { it == 'm' })
    val stacks1 = parseInputStacks(stacksStr)

    val moveRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")
    val moves = movesStr.split(System.lineSeparator()).map {
        val matchResult = moveRegex.matchEntire(it)
        if (matchResult != null) {
            val group = matchResult.groupValues
            Move(group[1].toInt(), group[2].toInt(), group[3].toInt())
        } else throw Exception("Failed to match move regex")
    }

    // Solve Part 1
    moves.forEach{
        while (it.quantity > 0) {
            it.quantity = it.quantity - 1
            val temp = stacks1[it.from-1].pop()
            stacks1[it.to - 1].push(temp)
        }
    }
    println("Result part1: ${stacks1.joinToString { it.peek() }}")
}

fun solve2(input: String){
    val stacksEndIdx = input.indexOfFirst { it == '1' }
    val stacksStr = input.substring(0, stacksEndIdx - 3)
    val movesStr = input.substring(input.indexOfFirst { it == 'm' })

    val stacks2 = parseInputStacks(stacksStr)
    val moveRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")
    val moves = movesStr.split(System.lineSeparator()).map {
        val matchResult = moveRegex.matchEntire(it)
        if (matchResult != null) {
            val group = matchResult.groupValues
            Move(group[1].toInt(), group[2].toInt(), group[3].toInt())
        } else throw Exception("Failed to match move regex")
    }

    // Solve Part 2
    moves.forEach{
        val tempStack = Stack<String>()
        while (it.quantity > 0) {
            it.quantity = it.quantity - 1
            tempStack.push(stacks2[it.from-1].pop())
        }
        while (tempStack.isNotEmpty()) {
            stacks2[it.to - 1].push(tempStack.pop())
        }
    }

    println("Result part2: ${stacks2.joinToString { it.peek() }}")
}