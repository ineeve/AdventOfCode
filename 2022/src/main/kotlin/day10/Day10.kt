package day10

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day10/input10.txt").readText()
    solve(input)
}

fun solve(input: String): Int {
    val instructions = input.split(System.lineSeparator())
    val reg = Regex("(noop)|(addx (-?\\d+))")
    // Array with the increments that should be performed at each cycle
    val xIncrements =  instructions.flatMap {
        val matchResult = reg.matchEntire(it)
        when(matchResult!!.groupValues[0]) {
            "noop" -> listOf(0)
            else -> listOf(0, matchResult.groupValues[3].toInt())
        }
    }
    var acc = 1
    val xValues: List<Int> = xIncrements.map {
        acc += it
        acc
    }

    val p1 = (20..220 step 40).fold(0) {res, idx -> res + xValues[idx-2]*idx}
    println("p1: $p1")

    val screen = (1 .. xValues.size).map { cycle ->
        val xInCycle: Int = if (cycle > 1) xValues[cycle -2] else 1
        if (cycle in (xInCycle-1..xInCycle+1)) '#' else '.'
    }

    val p2 = screen.chunked(40).joinToString(System.lineSeparator())
    println(p2)
    return p1
}