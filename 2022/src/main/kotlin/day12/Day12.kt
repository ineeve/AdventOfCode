package day12

import java.io.File

data class Pos(val row: Int, val col: Int)

data class Grid(val heights: List<List<Int>>, val start: Pos, val end: Pos) {
    private val costs = (heights.indices).map { heights[0].indices.map { Int.MAX_VALUE }.toMutableList() }
    fun heightAt(pos: Pos): Int {
        return heights[pos.row][pos.col]
    }

    fun costAt(pos: Pos): Int {
        return costs[pos.row][pos.col]
    }

    fun setCost(pos: Pos, v: Int) {
        costs[pos.row][pos.col] = v
    }
}

fun main() {
    val input = File("src/main/kotlin/day12/input12.txt").readText()
    part1(input)
    part2(input)
}

fun parseInputGrid(input: String): Grid {
    var start = Pos(0,0)
    var end = Pos(0,0)
    val heights = input.split(System.lineSeparator()).mapIndexed { row, line ->
        line.toCharArray().mapIndexed { col, elevation ->
            if (elevation.isLowerCase()) {
                elevation.code - 'a'.code
            } else if (elevation == 'S') {
                start = Pos(row,col)
                0
            } else {
                end = Pos(row, col);
                'z'.code-'a'.code
            }
        }
    }
    return Grid(heights, start, end)
}

fun part1(input: String): Int {

    val grid = parseInputGrid(input)
    grid.setCost(grid.start, 0)
    walkGrid(grid.start, grid.end, grid)
    val p1 = grid.costAt(grid.end)
    println("p1: $p1")
    return p1
}

fun part2(input: String): Int {
    val grid = parseInputGrid(input)
    grid.setCost(grid.end, 0)
    walkGrid(grid.end, Pos(-1,-1), grid, false)
    val zeroHeightsCosts = grid.heights.flatMapIndexed { row, line ->
        line.mapIndexed { col, height ->
            Pair(
                Pos(row, col),
                height
            )
        }.filter { it.second == 0 }.map { Pair(it.first, grid.costAt(it.first)) }
    }
    val p2 = zeroHeightsCosts.minByOrNull { it.second }!!.second
    println("p2: $p2")
    return p2
}
fun walkGrid(pos: Pos, endPos: Pos, grid: Grid, upHill: Boolean = true) {
    if (pos == endPos) return
    branch(pos, 1, 0, endPos, grid, upHill)  //branch right
    branch(pos, -1, 0, endPos, grid, upHill)  //branch left
    branch(pos, 0, 1, endPos, grid, upHill)  //branch down
    branch(pos, 0, -1, endPos, grid, upHill)  //branch up
}

fun branch(pos: Pos, x: Int, y: Int, endPos: Pos, grid: Grid, upHill: Boolean) {
    if (pos.row + y !in (grid.heights.indices)) return;
    if (pos.col + x !in (grid.heights[0].indices)) return;
    val cost = grid.costAt(pos)
    val elevation = grid.heightAt(pos)
    val newPos = Pos(pos.row + y, pos.col + x)
    val newElevation = grid.heightAt(newPos)
    if (upHill && newElevation > elevation + 1) return
    if (!upHill && newElevation < elevation - 1) return
    val newCost = grid.costAt(newPos)
    if (newCost <= cost+1) return
    grid.setCost(newPos, cost + 1)
    walkGrid(newPos, endPos, grid, upHill)
}

