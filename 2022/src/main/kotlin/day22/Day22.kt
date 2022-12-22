package day22

import java.io.File

enum class Facing {
    Right,
    Down,
    Left,
    Up
}
enum class Rotation {
    CC,
    CW
}

enum class Cell {
    Wall,
    Path,
    Empty
}

data class RC(var row: Int, var col: Int)
data class RCV(val row: Int, val col: Int, val value: Char)

fun main() {
    val file = File("src/main/kotlin/day22/input22.txt")
    val input = file.readText()
    val mapAndInstructions = input.split("\n\n")
    val map = parseMap(mapAndInstructions[0])
    val (steps, directions) = parseInstructions(mapAndInstructions[1])
    val startCol = map[0].indexOfFirst { it == Cell.Path }
    var pos = RC(0, startCol)
    var currentDirection = RC(0, 1)
    steps.forEachIndexed { idx, nsteps ->
        pos = move(pos, currentDirection, nsteps, map)
        if (idx < directions.size) {
            currentDirection = getNextDir(currentDirection, directions[idx])
        }
    }
    val facingVal = facing(currentDirection).ordinal
    val p1 = 1000*(pos.row + 1) + 4*(pos.col+1) + facingVal
    println("P1 $p1")
}

fun facing(dir: RC): Facing {
    return when(dir) {
        RC(0,1)->Facing.Right
        RC(1,0)->Facing.Down
        RC(0,-1)->Facing.Left
        RC(-1,0)->Facing.Up
        else -> throw Exception("Invalid dir")
    }
}

fun getNextDir(curDir: RC, rotation: Rotation): RC {
    return if (rotation == Rotation.CC) {
        when (curDir) {
            RC(0, 1) -> RC(1, 0)
            RC(1, 0) -> RC(0, -1)
            RC(0, -1) -> RC(-1, 0)
            RC(-1, 0) -> RC(0, 1)
            else -> throw Exception("Unexpected direction")
        }
    } else when (curDir) {
        RC(0, 1) -> RC(-1, 0)
        RC(-1, 0) -> RC(0, -1)
        RC(0, -1) -> RC(1, 0)
        RC(1, 0) -> RC(0, 1)
        else -> throw Exception("Unexpected direction")
    }
}


fun mapAt(rc: RC, map: List<List<Cell>>): Cell {
    if (rc.row == -1) rc.row = map.size - 1
    else if (rc.row == map.size) rc.row = 0
    else if (rc.col == -1) rc.col = map[0].size - 1
    else if (rc.col == map[0].size) rc.col = 0
    return map[rc.row][rc.col]
}

fun move(pos: RC, dir: RC, nsteps: Int, map: List<List<Cell>>): RC {
    return (1..nsteps).fold(pos) { curPos, _ ->
        val nextPos = RC(curPos.row + dir.row, curPos.col + dir.col)
        when (mapAt(nextPos, map)) {
            Cell.Path -> nextPos
            Cell.Wall -> curPos
            else -> wrapMap(curPos, dir, map)
        }
    }
}

fun wrapMap(pos: RC, dir: RC, map: List<List<Cell>>): RC {
    val nextPos = when (dir) {
        RC(0, 1) -> RC(pos.row, map[pos.row].indexOfFirst { it != Cell.Empty })
        RC(0, -1) -> RC(pos.row, map[pos.row].indexOfLast { it != Cell.Empty })
        RC(1, 0) -> RC(map.indexOfFirst { r -> r[pos.col] != Cell.Empty }, pos.col)
        RC(-1, 0) -> RC(map.indexOfLast { r -> r[pos.col] != Cell.Empty }, pos.col)
        else -> throw Exception("Unexpected direction")
    }
    if (mapAt(nextPos, map) == Cell.Wall) return pos
    return nextPos
}

fun parseMap(input: String): List<List<Cell>> {
    val lines = input.split(System.lineSeparator())
    val cols = lines.maxOfOrNull { it.length } as Int
    val map = lines.mapIndexed { row, it ->
        it.toCharArray().mapIndexed { col, v ->
            RCV(row, col, v)
        }
    }

    return (lines.indices).map { r ->
        (0 until cols).map { c ->
            val rcv = map[r].find { it.col == c }
            if (rcv == null) Cell.Empty
            else when (rcv.value) {
                '#' -> Cell.Wall
                '.' -> Cell.Path
                else -> Cell.Empty
            }
        }
    }
}

fun parseInstructions(input: String): Pair<List<Int>, List<Rotation>> {
    val steps = input.split(Regex("[A-Z]")).filter { it.isNotEmpty() }.map { it.toInt() }
    val rotations = input.split(Regex("\\d+")).filter { it.isNotEmpty() }.map {
        when (it) {
            "R" -> Rotation.CC
            else -> Rotation.CW
        }
    }
    println(steps)
    println(rotations)
    return Pair(steps, rotations)
}