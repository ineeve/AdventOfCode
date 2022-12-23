package day23

import java.io.File
import java.util.stream.Collectors

enum class Dir {
    N,
    S,
    W,
    E
}

data class RC(val row: Int, val col: Int)

fun main() {
    val file = File("src/main/kotlin/day23/input23.txt")
    part1(file)
    part2(file)
}

fun part2(file:File) {
    val elves = parseInput(file)
    val directions = ArrayDeque<Dir>()
    directions.addAll(listOf(Dir.N,Dir.S,Dir.W,Dir.E))
    val p2 = (1..10000).first {r ->
        val elvesMoving = playRound(elves, directions)
        println("Finished round $r, $elvesMoving elves moved")
        elvesMoving == 0
    }
    println("P2: $p2")
}

fun part1(file: File) {
    val elves = parseInput(file)
    val directions = ArrayDeque<Dir>()
    directions.addAll(listOf(Dir.N,Dir.S,Dir.W,Dir.E))
    val rounds = 10
    println("Initial State")
    //printMap(elves)
    (1..rounds).forEach {round ->
        playRound(elves, directions)
        println("End Of Round $round")
        //printMap(elves)
    }
    val minCol = elves.minOf { rc -> rc.col }
    val maxCol = elves.maxOf { rc -> rc.col }
    val minRow = elves.minOf { rc -> rc.row }
    val maxRow = elves.maxOf { rc -> rc.row }
    val cols = maxCol - minCol + 1
    val rows = maxRow - minRow + 1
    println("Cols: $cols, Rows: $rows")
    val rectSize = cols * rows
    val emptySpots = rectSize - elves.count()
    println("P1: $emptySpots")
}

fun printMap(elves: HashSet<RC>) {
    val minCol = elves.minOf { rc -> rc.col }
    val maxCol = elves.maxOf { rc -> rc.col }
    val minRow = elves.minOf { rc -> rc.row }
    val maxRow = elves.maxOf { rc -> rc.row }
    val rowsToPrint = (minRow..maxRow).map { row -> (minCol..maxCol).map { col ->
        if (elves.contains(RC(row, col))) '#' else '.' }.joinToString(" ") }
    rowsToPrint.forEach { println(it) }
}

fun playRound(elves: HashSet<RC>, directions: ArrayDeque<Dir>): Int {
    val roundDirs = directions.stream().map { it }.collect(Collectors.toList())
    val proposals = elves.map { elf ->
        val othersInNorth = (elf.col-1..elf.col+1).any { elves.contains(RC(elf.row-1, it)) }
        val othersInSouth = (elf.col-1..elf.col+1).any { elves.contains(RC(elf.row+1, it)) }
        val othersInWest = (elf.row-1..elf.row+1).any { elves.contains(RC(it, elf.col-1)) }
        val othersInEast = (elf.row-1..elf.row+1).any { elves.contains(RC(it, elf.col+1)) }
        if (othersInNorth || othersInSouth || othersInWest || othersInEast) {
            try {
                val movingDir = roundDirs.first {
                    when (it) {
                        Dir.N -> !othersInNorth
                        Dir.S -> !othersInSouth
                        Dir.E -> !othersInEast
                        Dir.W -> !othersInWest
                    }
                }
                val nextPos = when(movingDir) {
                    Dir.N -> RC(elf.row-1, elf.col)
                    Dir.S -> RC(elf.row+1, elf.col)
                    Dir.E -> RC(elf.row, elf.col+1)
                    Dir.W -> RC(elf.row, elf.col-1)
                }
                Pair(elf, nextPos)
            } catch (e: NoSuchElementException) {
                // stay
                Pair(elf, elf)
            }
        } else {
            Pair(elf,elf)
        }

    }
    val nextPosMap = proposals.groupBy { (_, nextPos) -> nextPos }
    val elvesMoved = proposals.count { (prevPos, nextPos) ->
        if (nextPosMap[nextPos]?.size == 1 && prevPos != nextPos) {
            // move this elf
            elves.remove(prevPos)
            elves.add(nextPos)
            true
        } else false
    }
    directions.addLast(directions.removeFirst())
    return elvesMoved
}

fun parseInput(file: File): HashSet<RC> {
    return file.readLines().flatMapIndexed { row, it -> it.toCharArray().mapIndexed { col, c -> Pair(RC(row, col), c) }}.filter { (rc, c)  -> c=='#'}.map { (rc,_)->rc }.toHashSet()
}
