package day02

import java.io.File

const val LOOSE = 'X'
const val DRAW = 'Y'
const val WIN = 'Z'

fun roundScore(enemyMove: Char, myMove: Char): Int {
    return when(enemyMove) {
        'A' -> if (myMove == 'X') 3 else if  (myMove == 'Y') 6 else 0;
        'B' -> if (myMove == 'Y') 3 else if (myMove == 'X')  0; else 6;
        'C' -> if (myMove == 'Z') 3 else if (myMove == 'X')  6; else 0;
        else -> 0;
    }
}

fun shapeScore(c: Char): Int {
    return when(c) {
        'X' -> 1
        'Y' -> 2
        'Z' -> 3
        else -> 0
    }
}

fun calculateScore(input: String): Int {
    val hands = input.split(System.lineSeparator())
        .map { it.split(" ")}
    val score = hands.sumOf { l -> roundScore(l[0][0], l[1][0]) + shapeScore(l[1][0]) }
    println("Result Part 1: $score")
    return score
}

fun myMovePart2(enemyMove: Char, objective: Char): Char {
    return when(enemyMove) {
        'A' -> when(objective) {
            LOOSE -> 'Z'
            DRAW -> 'X'
            WIN -> 'Y'
            else -> throw Exception("Unexpected Objective")
        }
        'B' -> when(objective) {
            LOOSE -> 'X'
            DRAW -> 'Y'
            WIN -> 'Z'
            else -> throw Exception("Unexpected Objective")
        }
        'C' -> when(objective) {
            LOOSE -> 'Y'
            DRAW -> 'Z'
            WIN -> 'X'
            else -> throw Exception("Unexpected Objective")
        }

        else -> throw Exception("Unexpected Enemy Move")
    }
}

fun calculateScorePart2(input: String): Int {
    val hands = input.split(System.lineSeparator())
        .map { it.split(" ")}
    val score = hands.sumOf { l -> roundScore(l[0][0], myMovePart2(l[0][0], l[1][0])) + shapeScore(myMovePart2(l[0][0], l[1][0])) }
    println("Result Part 2: $score")
    return score
}

fun main(args: Array<String>) {
    val input = File("src/main/kotlin/day02/input02.txt").readText()
    calculateScore(input)
    calculateScorePart2(input)
}