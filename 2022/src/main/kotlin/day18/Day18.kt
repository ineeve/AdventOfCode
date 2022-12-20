package day18

import java.io.File
import java.lang.Math.abs

fun main() {
    val file = File("src/main/kotlin/day18/input18.txt")
    part1(file)
}

fun areAdjacent(cube1: List<Int>, cube2: List<Int>): Boolean {
    return (kotlin.math.abs(cube2[0] - cube1[0]) + kotlin.math.abs(cube2[1] - cube1[1]) + kotlin.math.abs(cube2[2] - cube1[2]) == 1)
}

fun part1(file: File) {
    val cubes = file.readLines().map { it.split(",").map { coord -> coord.toInt() } }
    println(cubes)
    val p1 = cubes.foldIndexed(0) {idx, acc, cube1 ->
        acc + (0..idx).fold(0) {acc2, cube2Idx ->
            if (areAdjacent(cube1, cubes[cube2Idx])) acc2 - 2
            else acc2
        } + 6
    }
    println(p1)

}