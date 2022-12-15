package day14

import day11.solveP2
import java.io.File
import java.lang.IndexOutOfBoundsException

enum class PointType {
    Rock,
    Air,
    Sand,
    Source
}

class Grid {
    val sandSource: XY
    val cols: Int
    var rows: Int
    constructor(rows: Int, cols: Int, sandSource: XY) {
        this.rows = rows
        this.cols = cols
        this.sandSource = sandSource
        this.grid = (0 until rows).map { (0 until cols).map { PointType.Air } as MutableList<PointType> } as ArrayList
        this.set(sandSource, PointType.Source)
    }

    private val grid: ArrayList<MutableList<PointType>>

    fun at(xy: XY): PointType {
        return grid[xy.y][xy.x]
    }

    fun set(xy: XY, pt: PointType) {
        grid[xy.y][xy.x] = pt
    }

    fun addRow(pt: PointType) {
        this.grid.add((0 until cols).map { pt } as MutableList<PointType>)
        rows++
    }

    override fun toString(): String {
        return grid.joinToString(System.lineSeparator()) { line ->
            line.map { pt ->
                when (pt) {
                    PointType.Rock -> '#'
                    PointType.Air -> '.'
                    PointType.Sand -> 'o'
                    PointType.Source -> '+'
                }
            }.joinToString(" ")
        }
    }
}

data class XY(var x: Int, val y: Int)

var sandCounter = 0
fun main() {
    val input = File("src/main/kotlin/day14/input14.txt")
    //solveP1(input)
    solveP2(input)
}

fun solveP1(input: File): Int {
    sandCounter = 0
    val grid = parseInput(input)
    try {
        repeat(1000){
            addSand(grid)
        }
    } catch (exception: IndexOutOfBoundsException) {
        println("Sand ${sandCounter+1} is falling through the abyss")
    }

    println(grid)
    return sandCounter
}

fun solveP2(input: File): Int {
    sandCounter = 0
    val sideOffsetCols = 200
    val grid = parseInput(input, sideOffsetCols)
    grid.addRow(PointType.Air)
    grid.addRow(PointType.Rock)
    val sandSource = grid.sandSource
    try {
        while(grid.at(sandSource) != PointType.Sand){
            addSand(grid)
        }
    } catch (exception: IndexOutOfBoundsException) {
        println("Unexpected sand leaving through the sides ${sandCounter}")
    }

    println(grid)
    println("Solved P2 with $sandCounter sand grains")
    return sandCounter
}

fun addSand(grid: Grid) {
    var sandXY = XY(grid.sandSource.x, grid.sandSource.y)
    var nextSandXY = step(sandXY, grid)
    while(nextSandXY != sandXY) {
       sandXY = nextSandXY
       nextSandXY = step(sandXY, grid)
    }
    grid.set(sandXY, PointType.Sand)
    sandCounter++
}

fun step(sandXY: XY, grid: Grid): XY {
    val below = XY(sandXY.x, sandXY.y+1)
    val leftDiag = XY(sandXY.x-1, sandXY.y+1)
    val rightDiag = XY(sandXY.x+1, sandXY.y+1)
    if (grid.at(below) == PointType.Air) return below
    if (grid.at(leftDiag) == PointType.Air) return leftDiag
    if (grid.at(rightDiag) == PointType.Air) return rightDiag
    return sandXY
}

fun parseInput(input: File, sideOffsetCols: Int = 0): Grid {
    val rockLines = input.readLines().map {lineStr ->
        lineStr.split("->").map { xy ->
            val pair = xy.trim().split(',')
            XY(pair.first().toInt(),pair.last().toInt())
        }
    }
    println(rockLines)
    val minX = rockLines.minOf { rockLine -> rockLine.minOf { xy -> xy.x } }
    val maxX = rockLines.maxOf { rockLine -> rockLine.maxOf { xy -> xy.x } }
    val maxY = rockLines.maxOf { rockLine -> rockLine.maxOf { xy -> xy.y }}
    val cols = maxX - minX + 1 + 2*sideOffsetCols
    println("minX: $minX, maxX: $maxX, maxY: $maxY")
    rockLines.forEach{line -> line.forEach { xy -> xy.x -= minX - sideOffsetCols }}
    println(rockLines)
    val sandSource = XY(500-minX+sideOffsetCols, 0)
    val grid = Grid(maxY+1, cols, sandSource)
    rockLines.forEach{line -> line.windowed(2){
        val vert1 = it.first()
        val vert2 = it.last()
        val rocksXY = (minOf(vert1.x,vert2.x)..maxOf(vert1.x,vert2.x)).flatMap { x ->
            (minOf(vert1.y,vert2.y) .. maxOf(vert1.y,vert2.y)).map { y -> XY(x,y) }
        }
        rocksXY.forEach{xy -> grid.set(xy, PointType.Rock)}
    } }

    return grid
}