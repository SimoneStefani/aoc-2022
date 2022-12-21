import kotlin.math.max
import kotlin.math.min

fun main() {
    data class State(
        val oreRobots: Int = 1,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0,
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geode: Int = 0,
        val time: Int
    ) {
        fun next(
            oreRobots: Int = this.oreRobots,
            clayRobots: Int = this.clayRobots,
            obsidianRobots: Int = this.obsidianRobots,
            geodeRobots: Int = this.geodeRobots,
            ore: Int = this.ore + this.oreRobots,
            clay: Int = this.clay + this.clayRobots,
            obsidian: Int = this.obsidian + this.obsidianRobots,
            geode: Int = this.geode + this.geodeRobots,
        ) = State(oreRobots, clayRobots, obsidianRobots, geodeRobots, ore, clay, obsidian, geode, time - 1)
    }

    data class Blueprint(
        val id: Int,
        val oreOreCost: Int,
        val clayOreCost: Int,
        val obsidianOreCost: Int,
        val obsidianClayCost: Int,
        val geodeOreCost: Int,
        val geodeObsidianCost: Int
    ) {
        val maxOreCost = maxOf(oreOreCost, clayOreCost, obsidianOreCost, geodeOreCost)

        fun nextState(currentState: State) = buildList {
            add(currentState.next())

            if (currentState.ore >= oreOreCost) add(
                currentState.next(
                    oreRobots = currentState.oreRobots + 1,
                    ore = currentState.ore - oreOreCost + currentState.oreRobots
                )
            )

            if (currentState.ore >= clayOreCost) add(
                currentState.next(
                    clayRobots = currentState.clayRobots + 1,
                    ore = currentState.ore - clayOreCost + currentState.oreRobots
                )
            )

            if (currentState.ore >= obsidianOreCost && currentState.clay >= obsidianClayCost) add(
                currentState.next(
                    obsidianRobots = currentState.obsidianRobots + 1,
                    ore = currentState.ore - obsidianOreCost + currentState.oreRobots,
                    clay = currentState.clay - obsidianClayCost + currentState.clayRobots
                )
            )

            if (currentState.ore >= geodeOreCost && currentState.obsidian >= geodeObsidianCost) add(
                currentState.next(
                    geodeRobots = currentState.geodeRobots + 1,
                    ore = currentState.ore - geodeOreCost + currentState.oreRobots,
                    obsidian = currentState.obsidian - geodeObsidianCost + currentState.obsidianRobots
                )
            )
        }

        fun constraint(state: State): State {
            val nextTime = state.time - 1
            return state.copy(
                oreRobots = minOf(state.oreRobots, maxOreCost),
                clayRobots = minOf(state.clayRobots, obsidianClayCost),
                obsidianRobots = minOf(state.obsidianRobots, geodeObsidianCost),
                ore = min(maxOreCost * state.time - nextTime * minOf(state.oreRobots, maxOreCost), state.ore),
                clay = min(obsidianClayCost * state.time - nextTime * minOf(state.clayRobots, obsidianClayCost), state.clay),
                obsidian = min(geodeObsidianCost * state.time - nextTime * minOf(state.obsidianRobots, geodeObsidianCost), state.obsidian),
            )
        }

        fun work(time: Int) = work(State(time = time))
        fun work(state: State, visited: MutableMap<State, Int> = hashMapOf()): Int {
            if (state.time == 0) return state.geode
            var geodeCount = state.geode
            val constrained = constraint(state)
            visited[constrained]?.let { return it }
            nextState(state).forEach { geodeCount = max(work(it, visited), geodeCount) }
            visited[constrained] = geodeCount
            return geodeCount
        }
    }


    fun parseLine(line: String): Blueprint {
        val (id, oreCost, clayCost, obsidianOreCost, obsidianClayCost, geodeOreCost, geodeObsidianCost) = Regex("Blueprint (\\d+): Each ore robot costs (\\d+) ore. Each clay robot costs (\\d+) ore. Each obsidian robot costs (\\d+) ore and (\\d+) clay. Each geode robot costs (\\d+) ore and (\\d+) obsidian.")
            .matchEntire(line)?.destructured ?: throw IllegalArgumentException("Can't parse line: $line")
        return Blueprint(
            id.toInt(),
            oreCost.toInt(),
            clayCost.toInt(),
            obsidianOreCost.toInt(),
            obsidianClayCost.toInt(),
            geodeOreCost.toInt(),
            geodeObsidianCost.toInt()
        )
    }

    fun part1(input: List<String>): Int {
        return input.map(::parseLine).withIndex().sumOf { (idx, v) -> (idx + 1) * v.work(24) }
    }

    fun part2(input: List<String>): Int {
        return input.map(::parseLine).take(3).map { it.work(32) }.reduce { acc, curr -> acc * curr }
    }

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 33)
    check(part2(testInput) == 3472)

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
