package day21

import java.io.File

class Op(val left: String, val right: String, val op: Char)
fun main() {
    val file = File("src/main/kotlin/day21/input21.txt")
    val numberMonkeys = HashMap<String, Long>()
    val opMonkeys = HashMap<String, Op>()
    file.readLines().forEach {
        val opMonkeyRegex = Regex("([a-z]{4}): ([a-z]{4}) ([\\+\\-\\*/]) ([a-z]{4})")
        val numMonkeyRegex = Regex("([a-z]{4}): (\\d+)")
        val opMatch = opMonkeyRegex.matchEntire(it)
        val numMatch = numMonkeyRegex.matchEntire(it)
        if (opMatch != null) {
            val gv = opMatch.groupValues
            opMonkeys[gv[1]] = Op(gv[2],gv[4],gv[3][0])
        } else if (numMatch != null) {
            val gv = numMatch.groupValues
            numberMonkeys[gv[1]] = gv[2].toLong()
        } else throw Exception("No regex matched $it")
    }
    solveRiddle(numberMonkeys, opMonkeys)
    val p1 = numberMonkeys["root"]
    println("Part1 $p1")
}

fun solveRiddle(numberMonkeys: HashMap<String, Long>, opMonkeys: HashMap<String, Op>) {
    while (opMonkeys.isNotEmpty()) {
        val markedForRemoval = ArrayList<String>()
        opMonkeys.forEach {
            val monkey = it.key
            val op = it.value
            if (numberMonkeys.containsKey(op.left) && numberMonkeys.containsKey(op.right)) {
                markedForRemoval.add(monkey)
                val num = when(op.op) {
                    '+' -> numberMonkeys[op.left]?.plus(numberMonkeys[op.right]!!)
                    '-' -> numberMonkeys[op.left]?.minus(numberMonkeys[op.right]!!)
                    '*' -> numberMonkeys[op.left]?.times(numberMonkeys[op.right]!!)
                    '/' -> numberMonkeys[op.left]?.div(numberMonkeys[op.right]!!)

                    else -> throw Exception("Invalid operation")
                }
                numberMonkeys[monkey] = num!!
            }
        }
        markedForRemoval.forEach{opMonkeys.remove(it)}
    }
}