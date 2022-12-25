package day25

import java.io.File
import kotlin.math.pow

fun main() {
    val file = File("src/main/kotlin/day25/input25.txt")
    println(toSNAFU(23L))

    //(1L..1000L).forEach {
    //    val a = toDec(toSNAFU(it).reversed())
    //    if (a != it) throw Exception("Bad conversion of $it, $a") }
    val snafuNumbers = file.readLines().map { it.reversed() }
    val sumDec = snafuNumbers.sumOf { toDec(it) }
    println("Sum Dec: $sumDec")
    val p1 = toSNAFU(sumDec)
    println("P1: $p1")
}

fun toDec(n1: Char): Long {
    return when(n1) {
        '2' -> 2
        '1' -> 1
        '0' -> 0
        '-' -> -1
        '=' -> -2
        else -> throw Exception("ups")
    }
}

fun toDec(n: String): Long {
    return n.mapIndexed() {idx, char -> toDec(char) * 5.toDouble().pow(idx).toLong()}.sum()
}

fun toSNAFU(n: Long): String {
    var n1 = n
    val remainders = ArrayList<Char>()
    var acc = 0L
    while(n1>0 || acc > 0) {
        var remDec = (n1 % 5L) + acc
        acc = 0
        if (remDec > 2L) {
            acc++
        }
        remDec %= 5
        var remSNAFU = when(remDec) {
            3L -> '='
            4L -> '-'
            else -> toSNAFUChar(remDec)
        }

        remainders.add(remSNAFU)
        n1 /= 5
    }
    remainders.reverse()
    return remainders.joinToString("")
}


fun toSNAFUChar(i: Long): Char {
    return when(i) {
        0L -> '0'
        1L -> '1'
        2L -> '2'
        -1L -> '-'
        -2L -> '='
        else -> throw Exception("cant covert int to snafu")
    }
}