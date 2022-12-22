package day20

import java.io.File
import java.util.LinkedList
import kotlin.math.absoluteValue

class Node(val value: Long, var next: Node?, var prev: Node? ) {
    fun append(other: Node) {
        next = other
        other.prev = this
    }
    fun delete() {
        next?.prev = prev
        prev?.next = next
    }

    fun lookup(moves: Int): Node {
        if (moves == 0) return this
        return if (moves > 0) (1 .. moves.absoluteValue).fold(this){n,_ -> n.next!! }
        else (1 .. moves.absoluteValue).fold(this){n,_ -> n.prev!! }
    }
}
fun main() {
    val file = File("src/main/kotlin/day20/input20.txt")
    val inputSeq = file.readLines().map { it.toLong() }
    val p1 = decrypt(inputSeq)
    println("P1: $p1")
    val decryptKey = 811589153L
    val p2 = decrypt(inputSeq, 10, decryptKey)
    println("P2: $p2")
}

fun decrypt(list: List<Long>, rounds: Int = 1, key: Long = 1): Long {
    val decryptedList = list.map { it * key }
    val nodes = createNodes(decryptedList)
    (1..rounds).forEach { _ -> mix(nodes) }
    val zero = nodes.find { n -> n.value == 0L }
    return listOf(1000, 2000, 3000).sumOf { zero!!.lookup(it).value }
}

fun mix(nodes: List<Node>) {
    nodes.forEach { node ->
        node.delete()
        val offset = Math.floorMod(node.value, (nodes.size - 1))
        val left = node.prev!!.lookup(offset)
        val right = left.next!!
        left.append(node)
        node.append(right)
    }
}

fun createNodes(input: List<Long>): List<Node> {
    val nodes = input.map { v -> Node(v, null, null) }
    nodes.zipWithNext().forEach { (n1, n2) -> n1.append(n2) }
    nodes.last().append(nodes.first())
    return nodes
}

