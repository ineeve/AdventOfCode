package day24
import java.io.File
import kotlin.math.abs

data class RC(var row: Int, var col: Int)

data class BoardState(val minute: Int, val currPos: RC)

val blizzards = HashMap<RC, MutableList<Char>>()
val cacheBlizzards = HashMap<Int, HashMap<RC, MutableList<Char>>>()
var nRows = 0
var nCols = 0
val cache = HashMap<RC, HashSet<Int>>()
var startPos = RC(0,0)
var endPos = RC(0,0)

// increase jvm stack memory -Xss64m
fun main() {
    val file = File("src/main/kotlin/day24/input24.txt")

    val (positions, boardSize) = parseInput(file, blizzards)
    startPos = positions.first
    endPos = positions.second
    nRows = boardSize.first
    nCols = boardSize.second
    val lcm = lcm(nRows - 2, nCols - 2)
    println("nRows: $nRows, nCols: $nCols")

    println("LCM: $lcm")
    val bestExec = recursiveSearch(startPos, endPos,0, lcm, 300)
    println("P1: ${bestExec.minute}")
    currentMinMinute = Int.MAX_VALUE
    cache.clear()
    val backToStart = recursiveSearch(endPos, startPos, bestExec.minute, lcm, 800)
    println("Back to Start: ${backToStart.minute}, ${backToStart.currPos}")
    currentMinMinute = Int.MAX_VALUE
    cache.clear()
    val backToEnd = recursiveSearch(startPos, endPos, backToStart.minute, lcm, 1000)

    println("P2: ${backToEnd.minute}")
}

fun blizzardsAt(minute: Int, lcm: Int): HashMap<RC, MutableList<Char>> {
    val steps = minute % lcm
    if (cacheBlizzards.containsKey(steps)) return cacheBlizzards[steps]!!
    val map = HashMap<RC, MutableList<Char>>()

    val newBlizzards = blizzards.flatMap {
        moveBlizzardsInCell(it.toPair(), steps, nRows, nCols)
    }

    newBlizzards.forEach {
        map.putIfAbsent(it.first, mutableListOf<Char>())
        map[it.first]!!.add(it.second)
    }
    cacheBlizzards[steps]=map
    return map
}

fun lcm(n1: Int, n2: Int): Int {
    var gcd = 1

    var i = 1
    while (i <= n1 && i <= n2) {
        // Checks if i is factor of both integers
        if (n1 % i == 0 && n2 % i == 0)
            gcd = i
        ++i
    }

    return n1 * n2 / gcd
}

var currentMinMinute = Int.MAX_VALUE


fun recursiveSearch(currPos: RC, objective: RC, minute: Int, lcm: Int, maxDepth: Int): BoardState {
    if (minute > maxDepth) return  BoardState(minute, currPos)
    if (cache.containsKey(currPos) && cache[currPos]!!.contains(minute % lcm)) {
        return BoardState(minute, currPos)
    } else {
        cache.putIfAbsent(currPos, HashSet())
        cache[currPos]!!.add(minute % lcm)
    }
    if (minute > currentMinMinute) return BoardState(minute, currPos)
    if (currPos == objective) {
        if (minute < currentMinMinute) {
            currentMinMinute = minute
        }
        return BoardState(minute-1, currPos)
    }
    val currBlizzards = blizzardsAt(minute, lcm)

    val downPos = RC(currPos.row+1, currPos.col)
    val upPos = RC(currPos.row-1, currPos.col)
    val leftPos = RC(currPos.row, currPos.col-1)
    val rightPos = RC(currPos.row, currPos.col+1)
    val potNextPos = listOf(currPos, downPos,upPos,leftPos,rightPos)
        .filter { canMoveTo(it, currBlizzards, nRows, nCols) }
    if (potNextPos.isEmpty()) return BoardState(minute, currPos)

    val nextStates =  potNextPos.map {
        recursiveSearch(it, objective,minute+1, lcm, maxDepth) }
        .filter { bstate -> bstate.currPos == objective }
    if (nextStates.isEmpty()) return BoardState(Int.MAX_VALUE, currPos)
    return nextStates.minBy { boardState -> boardState.minute }

}

fun distance(p1: RC, p2:RC): Int {
    return abs(p2.row-p1.row) + abs(p2.col-p1.col)
}

fun canMoveTo(pos: RC, blizzards: HashMap<RC, MutableList<Char>>, nRows: Int, nCols: Int): Boolean {
    if (pos == startPos) return true
    if (pos == endPos) return true
    if (blizzards.containsKey(pos)) return false
    if (pos.col <= 0 || pos.col >= nCols - 1) return false
    if (pos.row <= 0 || pos.row >= nRows - 1) return false
    return true
}

fun moveBlizzardsInCell(pos: Pair<RC, MutableList<Char>>, steps: Int, rows: Int, cols: Int): MutableList<Pair<RC, Char>>  {
    val row = pos.first.row
    val col = pos.first.col
    val stepsCol = steps % (cols-2)
    val stepsRow = steps % (rows-2)
    val newBlizzards = pos.second.map { dir ->
        when(dir) {
            '>' -> Pair(RC(row, col+stepsCol), dir)
            '<' -> Pair(RC(row, col-stepsCol), dir)
            'v' -> Pair(RC(row+stepsRow, col), dir)
            '^' -> Pair(RC(row-stepsRow, col), dir)
            else -> throw Exception("Unexpected direction")
        }
    } as MutableList
    newBlizzards.forEach { blizzard ->
        if (blizzard.first.col <= 0)
            blizzard.first.col = cols-2+blizzard.first.col
        else if (blizzard.first.col >= cols-1)
            blizzard.first.col = 1 + (blizzard.first.col % (cols-1))
        if (blizzard.first.row <= 0)
            blizzard.first.row = rows - 2 + blizzard.first.row
        else if (blizzard.first.row >= rows -1)
            blizzard.first.row = 1 + (blizzard.first.row % (rows-1))

        if (blizzard.first.col == 0 || blizzard.first.row == 0 ||
            blizzard.first.col == cols -1 || blizzard.first.row == rows - 1)
            throw Exception("bug")
    }

    return newBlizzards
}

fun parseInput(file: File, blizzards: HashMap<RC, MutableList<Char>>): Pair<Pair<RC,RC>, Pair<Int,Int>> {
    val valley = file.readLines().mapIndexed{
            rowIdx, line -> line.toCharArray().mapIndexed{
            colIdx, char -> Pair(RC(rowIdx, colIdx), char)}}
    valley.flatten().filter{ (_,char) -> char == '>' || char=='<' || char =='v' || char == '^'}
        .forEach { (rc, char) -> blizzards[rc]= mutableListOf(char)}
    val startingPos = valley[0].first { (rc,c) -> c == '.' }.first
    val endPos = valley.last().first { (rc,c) -> c == '.' }.first

    return Pair(Pair(startingPos, endPos), Pair(valley.size, valley.first().size))
}

fun printBoard(currPos: RC, endPos: RC, blizzards: HashMap<RC, MutableList<Char>>, nRows:Int, nCols:Int) {
    val boardLines = (0 until nRows).map { row -> (0 until nCols).map { col ->
        val p = RC(row, col)
        if (currPos==p) "C"
        else if (p==endPos) "E"
        else if (blizzards.containsKey(p)) {
            if (blizzards[p]!!.size == 1) blizzards[p]!![0].toString()
            else blizzards[p]!!.size.toString()
        } else if (row==0||col==0||row==nRows-1||col==nCols-1) "#"
        else "."
    } }
    boardLines.forEach { line -> println(line.joinToString(" ")) }
}