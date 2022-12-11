package day08

import java.io.BufferedReader
import java.io.File
import java.util.stream.Collectors

fun main() {
    val input = File("src/main/kotlin/day08/input08.txt").bufferedReader()
    solve(input)
}

fun solve(input: BufferedReader): Int {
    val treeMap = input.lines().map { it.split("").filter { s -> s.isNotEmpty() }.map { v -> v.toInt() } }
        .collect(Collectors.toList())

    val ncols = treeMap[0].size
    val nrows = treeMap.size
    val visibleOnEdge = (nrows * 2 + ncols * 2) - 4
    println("Map is ready, rows: $nrows, cols: $ncols")
    val coords = (1 until nrows - 1).flatMap { r -> (1 until ncols - 1).map { c -> Pair(r, c) } }
    val p1 = coords.count(){ isVisible(it.first, it.second, treeMap) } + visibleOnEdge
    println("Part 1: $p1")
    val p2 = coords.maxOf { scenicScore(it.first,it.second,treeMap) }
    println("Part 2: $p2")
    return p1
}

fun scenicScore(row: Int, col: Int, treeMap: List<List<Int>>): Int {
    val cols = treeMap[row].size
    val rows = treeMap.size
    val treeHeight = treeMap[row][col]
    val left = treeMap[row].subList(0, col).reversed()
    val right = treeMap[row].subList(col + 1, cols)
    val above = treeMap.subList(0, row).map { r -> r[col] }.reversed()
    val below = treeMap.subList(row + 1, rows).map { r -> r[col] }
    var leftBlock = left.indexOfFirst { it >= treeHeight } + 1
    var rightBlock = right.indexOfFirst { it >= treeHeight } + 1
    var aboveBlock = above.indexOfFirst { it >= treeHeight } + 1
    var belowBlock = below.indexOfFirst { it >= treeHeight } + 1
    if (leftBlock == 0) leftBlock = left.size
    if (rightBlock == 0) rightBlock = right.size
    if (aboveBlock == 0) aboveBlock = above.size
    if (belowBlock == 0) belowBlock = below.size
    return leftBlock*rightBlock*aboveBlock*belowBlock
}

fun isVisible(row: Int, col: Int, treeMap: List<List<Int>>): Boolean {
    val cols = treeMap[row].size
    val rows = treeMap.size
    val treeHeight = treeMap[row][col]
    val left = treeMap[row].subList(0, col).max()
    val right = treeMap[row].subList(col + 1, cols).max()
    val above = treeMap.subList(0, row).map { r -> r[col] }.max()
    val below = treeMap.subList(row + 1, rows).map { r -> r[col] }.max()

    return treeHeight > listOf(left, right, above, below).min()
}