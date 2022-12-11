package day09

import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

enum class AOCMove {
    UP,
    RIGHT,
    DOWN,
    LEFT
}

data class Cell(val x: Int, val y: Int ) {
    var visited = false
}


fun main() {
    val input = File("src/main/kotlin/day09/input09.txt").readText()
    val hMoves = parseHeadMoves(input)
    val nRows = hMoves.count { it == AOCMove.DOWN }
    val nCols = hMoves.count { it == AOCMove.RIGHT }
    val grid1 = (0 until nRows).flatMap { y -> (0 until nCols).map{ x -> Cell(x, y)} }
    val grid2 = (0 until nRows).flatMap { y -> (0 until nCols).map{ x -> Cell(x, y)} }
    println("nRows: $nRows, nCols: $nCols")
    part1(grid1, nRows, nCols, hMoves)
    part2(grid2, nRows, nCols, hMoves)
}

fun part2(grid: List<Cell>,
          nRows: Int,
          nCols: Int,
          hMoves: List<AOCMove>) {
    val startPos = grid[(nRows - 1) * nCols / 2 + nCols]
    val chain = ArrayList<Cell>()
    val chainSize = 10
    // Head -- 1 -- 2 -- 3 ... 8 -- 9
    repeat(chainSize) { chain.add(startPos) }
    hMoves.forEach{
        chain[0] = nextHeadPos(it, chain.first(), grid, nCols)
        (1 until chainSize)
            .forEach{ idx -> chain[idx] = tailNextPos(chain[idx],chain[idx-1],grid, nCols, idx == chainSize-1 ) }

    }
    val p2 = grid.count { it.visited }
    println("Tail visited $p2 cells")
}

fun part1(
    grid: List<Cell>,
    nRows: Int,
    nCols: Int,
    hMoves: List<AOCMove>
) {
    var tail = grid[(nRows - 1) * nCols / 2 + nCols]
    tail.visited = true
    var head = tail
    println("Start Pos: $tail")
    hMoves.forEach {
        head = nextHeadPos(it, head, grid, nCols)
        tail = tailNextPos(tail, head, grid, nCols, true)
    }
    val p1 = grid.count { it.visited }
    println("Tail visited $p1 cells")
}

fun distance(cell1: Cell, cell2: Cell): Double {
    return kotlin.math.sqrt((cell2.x - cell1.x).toDouble().pow(2)
            + (cell2.y - cell1.y).toDouble().pow(2))
}

fun tailNextPos(tail: Cell, head: Cell, grid: List<Cell>, nCols: Int, mark: Boolean): Cell {
    if (distance(tail, head) < 2) return tail
    val diffVec = Pair(head.x - tail.x, head.y - tail.y)
    val newX = tail.x + if (diffVec.first > 0) min(1, diffVec.first) else max(-1, diffVec.first)
    val newY = tail.y + if (diffVec.second > 0) min(1, diffVec.second) else max(-1, diffVec.second)
    val newPos = grid[(newY*nCols)+newX]
    if (mark) newPos.visited = true
    return newPos
}

fun nextHeadPos(move: AOCMove, head: Cell, grid: List<Cell>, nCols: Int): Cell {
    var newX = head.x
    var newY = head.y
    when (move) {
        AOCMove.LEFT -> newX--
        AOCMove.DOWN -> newY++
        AOCMove.RIGHT -> newX++
        AOCMove.UP -> newY--
    }
    return grid[(newY*nCols)+newX]
}

fun parseHeadMoves(input: String): List<AOCMove> {
    val moveRegex = Regex("([UDLR])\\s(\\d+)")
    val moves = input.split(System.lineSeparator()).flatMap { line ->
            val matchResult = moveRegex.matchEntire(line)
            val dir = matchResult!!.groupValues[1]
            val nMoves = matchResult.groupValues[2].toInt()
            when (dir) {
                "U" -> List(nMoves) {AOCMove.UP}
                "R" -> List(nMoves) {AOCMove.RIGHT}
                "L" -> List(nMoves) {AOCMove.LEFT}
                "D" -> List(nMoves) {AOCMove.DOWN}
                else -> throw Exception("Invalid move type")
            }
        }
    return moves
}