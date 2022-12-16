package day15

import java.io.File

data class XY(val x: Int, val y: Int)

fun distance(p1: XY, p2: XY): Int {
    return kotlin.math.abs(p2.x - p1.x) + kotlin.math.abs(p2.y - p1.y)
}

fun main() {
    val input = File("src/main/kotlin/day15/input15.txt")
    solveP1(input, 2000000)
}

fun solveP1(input: File, solveForRow: Int): Int {
    val lineRegex = Regex("Sensor at x=(\\d+), y=(\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")

    val pairs = input.readLines().map { line ->
        val matchRes = lineRegex.matchEntire(line) ?: throw Exception("Failed to match regex")
        val matches = matchRes.groupValues
        val sensor = XY(matches[1].toInt(), matches[2].toInt())
        val beacon = XY(matches[3].toInt(), matches[4].toInt())
        Pair(sensor, beacon)
    }
    val minX = pairs.minOf { minOf(it.first.x, it.second.x) }
    val minY = pairs.minOf { minOf(it.first.y, it.second.y) }
    val maxX = pairs.maxOf { maxOf(it.first.x, it.second.x) }
    val maxY = pairs.maxOf { maxOf(it.first.y, it.second.y) }
    val p1Row = solveForRow - minY
    val cols = maxX - minX + 1
    val rows = maxY - minY + 1
    val pairsTranslated = pairs.map { pair ->
        Pair(
            XY(pair.first.x - minX, pair.first.y - minY),
            XY(pair.second.x - minX,pair.second.y - minY)
        )
    }
    val pairsFlat = pairsTranslated.flatMap { listOf(it.first, it.second) }

    println("Computing no beacon zones")
    var counter = 0
    val noBeaconZones = pairsTranslated.flatMap { (sensor, beacon) ->
        val d = distance(sensor, beacon)
        println("No Beacons Zones progress: ${++counter} out of ${pairsTranslated.size}")
        (sensor.x - d..sensor.x + d)
            .map { x -> XY(x, p1Row) }
            .filter { xy -> distance(sensor, xy) <= d }
    }.toSet()


    println("Computing p1")
    val noBeaconMinX = noBeaconZones.minOf { it.x }
    val noBeaconMaxX = noBeaconZones.maxOf { it.x }

    val noBeaconsOnLine = (noBeaconMinX .. noBeaconMaxX).filter { col ->
        val xy = XY(col, p1Row)
        !pairsFlat.contains(xy) && noBeaconZones.contains(xy)
    }
    val p1 = noBeaconsOnLine.count()
    println("Part1: $p1")
    return p1
}