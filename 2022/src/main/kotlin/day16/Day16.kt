package day16

import java.io.File

fun main() {
    val file = File("src/main/kotlin/day16/input16.txt")
    val valves = parseInput(file)
    // Well, greedy doesn't work
    p1(valves)
}

fun p1(valves: HashSet<Valve>) {
    var step = 30
    var bestValve = valves.first { it.id == "AA" }

    while (step > 0) {
        bestValve = bestValve.getBestValveToOpenFirst(step, bestValve)
        println("Open valve ${bestValve.id} at step ${bestValve.openInCycle} with totalFlow: ${bestValve.totalFlow}")
        bestValve.opened = true
        step = bestValve.openInCycle
        valves.forEach { it.reset() }
    }
}

data class Valve(val id: String, val flow: Int, val tunnels: List<String>) {
    private var visited = false
    var totalFlow = -1
    var openInCycle = -1
    var opened = false
    var tunnelValves: ArrayList<Valve> = ArrayList()
    fun reset() {
        visited = false
        totalFlow = -1
        openInCycle = -1
    }
    fun getBestValveToOpenFirst(steps: Int, bestValve: Valve): Valve {
        if (steps <= 0) return bestValve
        if (visited) return bestValve
        visited = true
        val tunnelsBestValves = tunnelValves.map { it.getBestValveToOpenFirst(steps - 1, bestValve) }
        val bestTunnel = tunnelsBestValves.maxByOrNull { it.totalFlow }!!
        if (opened) return bestTunnel
        totalFlow = (steps-1)*flow
        if (totalFlow > bestTunnel.totalFlow) {
            openInCycle = steps-1
            return this
        }
        return bestTunnel
    }
}

fun parseInput(file: File): HashSet<Valve> {
    val regex = Regex("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? ((([A-Z]{2}(,\\s)?))+)")
    val valves = file.readLines().map { line ->
        val matchResult = regex.matchEntire(line)
        val valve = matchResult!!.groups[1]!!.value
        val flow = matchResult!!.groups[2]!!.value.toInt()
        val tunnels = matchResult!!.groups[3]!!.value.split(",").map { it.trim() }
        Valve(valve, flow, tunnels)
    }.toHashSet()
    valves.forEach{v ->
        val tunnelValves = v.tunnels.map { id -> valves.first { it.id == id } }
        v.tunnelValves.addAll(tunnelValves)
    }
    return valves
}