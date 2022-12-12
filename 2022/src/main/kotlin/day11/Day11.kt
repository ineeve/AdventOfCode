package day11

import java.io.File
import java.math.BigInteger
import java.util.*


class Monkey constructor(
    val operation: (a: BigInteger) -> BigInteger,
    val modTester: BigInteger,

    val throwTrue: Int,
    val throwFalse: Int
) {
    private val items: Queue<BigInteger> = LinkedList()
    var inspectCount = 0

    fun pop(): BigInteger? {
        if (!isEmpty()) inspectCount++
        return items.poll()
    }

    fun test(a: BigInteger): Boolean {
        return a % modTester == BigInteger.ZERO
    }

    fun isEmpty(): Boolean {return items.isEmpty()}

    fun add(x: BigInteger) {
        items.add(x)
    }

    override fun toString(): String {
        return "Inspected $inspectCount"
    }

    companion object {
        fun fromInput(input: String): Monkey {
            val itemsRegex = Regex("Starting items: ((\\d+,?\\s?)+)")
            val opRegex = Regex("Operation: new = old ([\\*\\+]) (old)?(\\d+)?")
            val testRegex = Regex("Test: divisible by (\\d+)")
            val trueRegex = Regex("If true: throw to monkey (\\d)")
            val falseRegex = Regex("If false: throw to monkey (\\d)")
            val itemsGroup = itemsRegex.find(input)!!.groupValues
            val items = itemsGroup[1].split(",").map { it.trim().toBigInteger() }
            val opGroup = opRegex.find(input)!!.groupValues
            val op: (a: BigInteger) -> BigInteger = when (opGroup[1]) {
                "*" -> { a -> a * if (opGroup[2] == "old") a else opGroup[3].toBigInteger()}
                "+" -> { a -> a + if (opGroup[2] == "old") a else opGroup[3].toBigInteger()}
                else -> throw Exception("Unexpected math operator")
            }
            val testDiv = testRegex.find(input)!!.groupValues[1].toBigInteger()
            val trueMonkey = trueRegex.find(input)!!.groupValues[1].toInt()
            val falseMonkey = falseRegex.find(input)!!.groupValues[1].toInt()
            val m = Monkey(op, testDiv, trueMonkey, falseMonkey)
            items.forEach{m.add(it)}
            return m
        }
    }
}

fun main() {
    val input =  File("src/main/kotlin/day11/input11.txt").readText()
    val monkeys1 = input.split("\r\n\r\n").map { Monkey.fromInput(it) }
    val monkeys2 = input.split("\r\n\r\n").map { Monkey.fromInput(it) }
    solveP1(monkeys1, 20)
    solveP2(monkeys2, 10000)
}

fun solveP1(monkeys: List<Monkey>, cycles: Int) {
    repeat(cycles){
        monkeys.forEach {m ->
            while (!m.isEmpty()) {
                var item = m.pop()!!
                item = m.operation(item) / (3).toBigInteger()
                val nextMonkey = if (m.test(item)) m.throwTrue else m.throwFalse
                monkeys[nextMonkey].add(item)
            }
        }
    }
    println(monkeys)
    val sortedMonkeys = monkeys.sortedByDescending { it.inspectCount }
    val monkeyBusiness: Long = sortedMonkeys[0].inspectCount.toLong() * sortedMonkeys[1].inspectCount.toLong()
    println("Result: $monkeyBusiness")
}

fun solveP2(monkeys: List<Monkey>, cycles: Int) {
    val composite = monkeys.fold(BigInteger.ONE){acc, monkey ->  acc * monkey.modTester}
    repeat(cycles){
        monkeys.forEach {m ->
            while (!m.isEmpty()) {
                var item = m.pop()!!
                if (item < BigInteger.ZERO) throw Exception("Negative unexpected")
                item = m.operation(item) % composite
                val nextMonkey = monkeys[if (m.test(item)) m.throwTrue else m.throwFalse]
                nextMonkey.add(item)
            }
        }
    }
    println(monkeys)
    val sortedMonkeys = monkeys.sortedByDescending { it.inspectCount }
    val monkeyBusiness: Long = sortedMonkeys[0].inspectCount.toLong() * sortedMonkeys[1].inspectCount.toLong()
    println("Result: $monkeyBusiness")
}

