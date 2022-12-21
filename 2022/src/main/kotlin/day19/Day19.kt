package day19

import java.io.File

enum class Res {
    ORE,
    CLAY,
    OBS,
    GEO
}
val allResources = listOf<Res>(Res.ORE,Res.CLAY, Res.OBS, Res.GEO)
class Blueprint(val id: Int, private val robotCosts: List<List<Int>>): Comparator<Blueprint> {
    private val robots = mutableListOf<Int>(1,0,0,0) // ore, clay, obsidian, geode
    val resources = mutableListOf<Int>(0,0,0,0) //ore,clay,obsidian,geode
    private var nextRobot: Res? = null
    fun cloneWithNewRobot(res: Res): Blueprint {
        val newBP = Blueprint(id, robotCosts)
        allResources.forEach{
            newBP.robots[it.ordinal] = robots[it.ordinal]
            newBP.resources[it.ordinal] = resources[it.ordinal]
        }
        newBP.resources[res.ordinal]-- // remove the resource that the robot is going to produce in the current step
        newBP.makeRobot(res)
        return newBP
    }
    fun canPurchaseRobot(res: Res): Boolean {
        return resources.filterIndexed { index, quantity -> quantity >= robotCosts[res.ordinal][index] }.size == 4
    }

    fun makeRobot(res: Res) {
        (0..3).forEach { resources[it] -= robotCosts[res.ordinal][it] }
        robots[res.ordinal]++
        nextRobot = null
    }

    fun collectResources() {
        (0..3).forEach { resources[it] += robots[it]}
    }

    fun stepsToBuildRobot(robot: Res): Int {
        val requirements = robotCosts[robot.ordinal]
        val resMissing = requirements.mapIndexed{ idx, it -> it - resources[idx] }
        val stepsRequired = resMissing.mapIndexed{i,r -> if (r ==0) 0 else kotlin.math.ceil(r.toDouble() / robots[i]).toInt() }
        return stepsRequired.max()
    }

    private fun targetRobot(): Res {
        if (robots[Res.CLAY.ordinal]==0) return Res.CLAY
        if (robots[Res.OBS.ordinal]==0) return Res.OBS
        return Res.GEO
    }

    fun nextRobot(): Res {
        if (nextRobot != null) return nextRobot as Res
        val target = targetRobot()
        val requirements = robotCosts[target.ordinal]
        if (canPurchaseRobot(target)) return target
        var resMissing = requirements.mapIndexed{ idx, it -> it - resources[idx] }
        val stepsRequired = resMissing.mapIndexed{i,r -> if (r ==0) 0 else kotlin.math.ceil(r.toDouble() / robots[i]).toInt() }
        val maxSteps = stepsRequired.max()
        val resMostReq = stepsRequired.indices.maxBy { stepsRequired[it] }
        // calculate steps if a new robot was build to handle the resource that is missing the most
        robots[resMostReq]++
        val stepsWithNewRobot = resMissing.mapIndexed{i,r -> kotlin.math.ceil((r + robotCosts[resMostReq][i]) / (robots[i].toDouble()))
            .toInt() + 1}
        robots[resMostReq]--
        val maxStepsWithNewRobot = stepsWithNewRobot.max()
        return if (maxSteps < maxStepsWithNewRobot) {
            target
        } else resource(resMostReq)
    }

    private fun resource(i: Int): Res {
        return when (i) {
            0 -> Res.ORE
            1 -> Res.CLAY
            2 -> Res.OBS
            3 -> Res.GEO
            else -> throw Exception("Invalid resource")
        }
    }

    override fun compare(o1: Blueprint?, o2: Blueprint?): Int {
        if (o1 == o2) return 0
        if (o1 == null) return -1
        if (o2 == null) return 1
        if (o1.resources[Res.GEO.ordinal] > o2.resources[Res.GEO.ordinal]) return 1
        if (o1.resources[Res.GEO.ordinal] == o2.resources[Res.GEO.ordinal]) return 0
        return -1
    }

}

fun main() {
    val file = File("src/main/kotlin/day19/input19.txt")
    val blueprints = parseInput(file)
    println(blueprints)
    val p1 = blueprints.fold(0) {acc,it ->
        val simulation = simulate(it, step = 1)
        val geodes = simulation.resources[Res.GEO.ordinal]
        println("Blueprint ${it.id} -> $geodes}")
        val quality = geodes * it.id
        acc + quality
    }
    println(p1)
}
fun simulate(blueprint: Blueprint, step: Int): Blueprint {
    if (step == 25) return blueprint
    val nextRobot = blueprint.nextRobot()

    if (blueprint.canPurchaseRobot(nextRobot)) {
        blueprint.collectResources()
        blueprint.makeRobot(nextRobot)
    } else
        blueprint.collectResources()

    return simulate(blueprint, step + 1)
}

fun parseInput(file: File): List<Blueprint> {
    val regex = Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.")
    return file.readLines().map {
        val matchResult = regex.matchEntire(it) ?: throw Exception("The regex is wrong")
        val gv = matchResult.groupValues
        val id = gv[1].toInt()
        val oreRobotCost = listOf(gv[2].toInt(),0,0,0)
        val clayRobotCost = listOf(gv[3].toInt(),0,0,0)
        val obsidianRobotCost = listOf(gv[4].toInt(), gv[5].toInt(), 0, 0)
        val geodeRobotCost = listOf(gv[6].toInt(),0,gv[7].toInt(), 0)
        val robotCosts = listOf(oreRobotCost,clayRobotCost,obsidianRobotCost,geodeRobotCost)
        Blueprint(id, robotCosts)
    }
}