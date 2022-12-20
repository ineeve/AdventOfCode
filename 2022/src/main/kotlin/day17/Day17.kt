package day17

import java.io.File

data class D17XY(val x: Int, val y: Int)
enum class D17Mat {
    FallingRock,
    StandingRock,
    Air,
}

enum class RockType {
    HL,
    VL,
    SQ,
    PL,
    L,
}
enum class D17Move {
    LEFT,
    RIGHT
}

class Board {
    val standingRocks: ArrayList<Rock> = ArrayList()
    private var fallingRock: Rock = HLRock()
    var maxGroundHeight = 0
    private val possibleRocks = listOf<RockType>(RockType.HL, RockType.PL, RockType.L,RockType.VL,RockType.SQ)
    private var rockIterator = possibleRocks.iterator()
    fun addRock(): Rock {
        if (!rockIterator.hasNext()) rockIterator = possibleRocks.iterator()
        val nextRockType = rockIterator.next()
        fallingRock = rockFactory(nextRockType)
        fallingRock.pos = D17XY(2, maxGroundHeight + 4)
        return fallingRock
    }

    private fun rockFactory(rockType: RockType): Rock {
        return when(rockType) {
            RockType.HL -> HLRock()
            RockType.PL -> PlusRock()
            RockType.L -> LRock()
            RockType.SQ -> SquareRock()
            RockType.VL -> VLRock()
        }
    }
}

interface Rock {
    val type: RockType
    var rockForm: List<List<D17Mat>>;
    var rockAbsForm: List<D17XY>?
    var pos: D17XY

    fun makeStanding() {
        rockForm = rockForm.map { line -> line.map { if (it == D17Mat.FallingRock) D17Mat.StandingRock else D17Mat.Air } }
        computeRockAbsForm()
    }
    fun computeRockAbsForm(): List<D17XY> {
        if (rockAbsForm != null) return rockAbsForm as List<D17XY>
        rockAbsForm = rockForm.flatMapIndexed { y, line ->
            line.mapIndexed{x, mat ->
                if (mat == D17Mat.Air) {D17XY(-1,-1)}
                else D17XY(pos.x + x, pos.y + y)}
                .filter { xy -> xy.x >= 0 && xy.y >= 0 }
        }
        return rockAbsForm as List<D17XY>
    }
    fun moveLeft(board: Board) {
        pos = D17XY(pos.x-1,pos.y)
        rockAbsForm = null
        if (testCollision(board)) {
            pos = D17XY(pos.x+1,pos.y)
            rockAbsForm = null
        }
    }
    fun moveRight(board: Board) {
        pos = D17XY(pos.x+1,pos.y)
        rockAbsForm = null
        if (testCollision(board)) {
            pos = D17XY(pos.x-1,pos.y)
            rockAbsForm = null
        }
    }
    // Returns true if rock hit the bottom
    fun moveDown(board: Board): Boolean {
        pos = D17XY(pos.x,pos.y-1)
        rockAbsForm = null
        if (testCollision(board)) {
            pos = D17XY(pos.x,pos.y+1)
            rockAbsForm = null
            makeStanding()
            board.maxGroundHeight = maxOf(board.maxGroundHeight, height() + pos.y - 1)
            board.standingRocks.add(this)
            return true
        }
        return false
    }
    fun width(): Int {return rockForm[0].size}

    fun height(): Int {return rockForm.size}
    fun testCollision(board: Board): Boolean {
        if (pos.x < 0) return true
        if (pos.x + width() > 7 ) return true
        if (pos.y == 0) return true
        if (board.maxGroundHeight < pos.y) return false
        val thisForm = computeRockAbsForm()
        return board.standingRocks.reversed().any { otherRock ->
            val otherRockForm = otherRock.computeRockAbsForm()
            otherRockForm.any { thisForm.contains(it)}
        }
    }
}

class PlusRock: Rock {
    override var pos = D17XY(0,0)
    override val type: RockType = RockType.PL
    override var rockForm = listOf(
        listOf(D17Mat.Air,D17Mat.FallingRock,D17Mat.Air),
        listOf(D17Mat.FallingRock,D17Mat.FallingRock,D17Mat.FallingRock),
        listOf(D17Mat.Air,D17Mat.FallingRock,D17Mat.Air))
    override var rockAbsForm: List<D17XY>? = null
}

class HLRock: Rock {
    override val type: RockType = RockType.HL
    override var rockAbsForm: List<D17XY>? = null
    override var pos = D17XY(0,0)
    override var rockForm = listOf(
        listOf(D17Mat.FallingRock, D17Mat.FallingRock, D17Mat.FallingRock, D17Mat.FallingRock)
    )
}

class VLRock: Rock {
    override val type: RockType = RockType.VL
    override var rockAbsForm: List<D17XY>? = null
    override var pos = D17XY(0,0)
    override var rockForm = listOf(
        listOf(D17Mat.FallingRock),
        listOf(D17Mat.FallingRock),
        listOf(D17Mat.FallingRock),
        listOf(D17Mat.FallingRock),
    )
}

class LRock: Rock {
    override val type: RockType = RockType.L
    override var rockAbsForm: List<D17XY>? = null
    override var pos = D17XY(0,0)
    override var rockForm = listOf(
        listOf(D17Mat.FallingRock, D17Mat.FallingRock, D17Mat.FallingRock),
        listOf(D17Mat.Air, D17Mat.Air, D17Mat.FallingRock),
        listOf(D17Mat.Air, D17Mat.Air, D17Mat.FallingRock)
    )
}

class SquareRock: Rock {
    override val type: RockType = RockType.SQ
    override var rockAbsForm: List<D17XY>? = null
    override var pos = D17XY(0,0)
    override var rockForm = listOf(
        listOf(D17Mat.FallingRock, D17Mat.FallingRock),
        listOf(D17Mat.FallingRock,D17Mat.FallingRock)
    )
}

fun main() {
    val file = File("src/main/kotlin/day17/input17.txt")
    val jetStream = parseInput(file.readText())
    var jetIt = jetStream.iterator()
    val board = Board()
    var totalRocks = 2022
    while (totalRocks > 0) {
        val fallingRock = board.addRock()
        totalRocks--
        do {
            if (!jetIt.hasNext()) jetIt = jetStream.iterator()
            when(jetIt.next()) {
                D17Move.LEFT -> fallingRock.moveLeft(board)
                else -> fallingRock.moveRight(board)
            }
        } while (!fallingRock.moveDown(board))
    }
    println("Over, max ground height: ${board.maxGroundHeight}")
}

fun parseInput(input: String): List<D17Move> {
    return input.toCharArray().map { when(it) {
        '<' -> D17Move.LEFT
        '>' -> D17Move.RIGHT
        else -> throw Exception("Unexpected char")
    } }
}