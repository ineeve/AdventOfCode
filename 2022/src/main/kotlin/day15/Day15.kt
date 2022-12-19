package day15

import java.io.File

data class XY(val x: Int, val y: Int)

fun distance(p1: XY, p2: XY): Int {
    return kotlin.math.abs(p2.x - p1.x) + kotlin.math.abs(p2.y - p1.y)
}

fun main() {
    val input = File("src/main/kotlin/day15/input15.txt")
    solveP1(input, 2000000)
    solveP2(input, 4000000)
}

fun parseInput(input: File): List<Pair<XY,XY>> {
    val lineRegex = Regex("Sensor at x=(\\d+), y=(\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")

    return input.readLines().map { line ->
        val matchRes = lineRegex.matchEntire(line) ?: throw Exception("Failed to match regex")
        val matches = matchRes.groupValues
        val sensor = XY(matches[1].toInt(), matches[2].toInt())
        val beacon = XY(matches[3].toInt(), matches[4].toInt())
        Pair(sensor, beacon)
    }
}


fun isDistressRow(row: Int, pairs: List<Pair<XY,Int>>): Int {
    var xCoverage = pairs.filter { (sensor, dist) -> row in (sensor.y - dist ..sensor.y + dist) }
        .map { (sensor, distToBeacon) ->
            val distToRow = distance(sensor, XY(sensor.x, row))
            val xMargin = distToBeacon - distToRow
            (sensor.x-xMargin..sensor.x+xMargin)
    }
    xCoverage = xCoverage.sortedBy { it.first }
    var mainXCoverage = IntRange(0,0)
    for (coverage in xCoverage) {
        if (coverage.first <= mainXCoverage.last && coverage.last > mainXCoverage.last) {
            mainXCoverage = IntRange(0, coverage.last)
        }
    }
    return mainXCoverage.last
}
fun solveP1(input: File, solveForRow: Int): Int {
    val pairs = parseInput(input)

    val pairsFlat = pairs.flatMap { listOf(it.first, it.second) }

    println("Computing no beacon zones")
    var counter = 0
    val noBeaconZones = pairs.flatMap { (sensor, beacon) ->
        val d = distance(sensor, beacon)
        println("No Beacons Zones progress: ${++counter} out of ${pairs.size}")
        (sensor.x - d..sensor.x + d)
            .map { x -> XY(x, solveForRow) }
            .filter { xy -> distance(sensor, xy) <= d }
    }.toSet()


    println("Computing p1")
    val noBeaconMinX = noBeaconZones.minOf { it.x }
    val noBeaconMaxX = noBeaconZones.maxOf { it.x }

    val noBeaconsOnLine = (noBeaconMinX .. noBeaconMaxX).filter { col ->
        val xy = XY(col, solveForRow)
        !pairsFlat.contains(xy) && noBeaconZones.contains(xy)
    }
    val p1 = noBeaconsOnLine.count()
    println("Part1: $p1")
    return p1
}

fun solveP2(input: File, searchSpaceSize: Int): Long {
    val pairs = parseInput(input)

    println("Running p2")
    val searchSpace = (0..searchSpaceSize)
    var col = -1
    val sensorDistPairs = pairs.map { (sensor, beacon) ->
        val d = distance(sensor, beacon)
        Pair(sensor, d)
    }

    val row = searchSpace.first {
        col = isDistressRow(it, sensorDistPairs)
        col < searchSpaceSize
    }
    col++
    println("Distress signal is in row $row, col $col")
    val p2 = col * 4000000L + row
    println("P2= $p2")
    return p2
}