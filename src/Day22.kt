import java.io.File
import java.lang.Math.floorMod

fun main() {
    data class Vector(val x: Int, val y: Int) {
        operator fun plus(other: Vector): Vector = Vector(x + other.x, y + other.y)

        fun move(direction: Int): Vector = this + when (direction) {
            0 -> Vector(0, -1)
            90 -> Vector(1, 0)
            180 -> Vector(0, 1)
            270 -> Vector(-1, 0)
            else -> throw IllegalArgumentException()
        }
    }

    operator fun List<String>.get(vector: Vector): Char = this[vector.y][vector.x]

    fun wrap2D(map: List<String>, position: Vector, direction: Int): Pair<Vector, Int> {
        val unwrapped = position.move(direction)
        val wrapped = when {
            unwrapped.y < 0 || direction == 0 && map[unwrapped] == ' ' ->
                Vector(unwrapped.x, map.indexOfLast { it.length > unwrapped.x && it[unwrapped.x] != ' ' })

            unwrapped.y >= map.size || direction == 180 && (unwrapped.x >= map[unwrapped.y].length || map[unwrapped] == ' ') ->
                Vector(unwrapped.x, map.indexOfFirst { it.length > unwrapped.x && it[unwrapped.x] != ' ' })

            unwrapped.x < 0 || direction == 270 && map[unwrapped] == ' ' ->
                Vector(map[unwrapped.y].indexOfLast { it != ' ' }, unwrapped.y)

            unwrapped.x >= map[unwrapped.y].length || direction == 90 && map[unwrapped] == ' ' ->
                Vector(map[unwrapped.y].indexOfFirst { it != ' ' }, unwrapped.y)

            else -> unwrapped
        }
        return Pair(wrapped, direction)
    }

    fun wrap3D(position: Vector, direction: Int): Pair<Vector, Int> {
        val move = (position.y / 50) * 3 + position.x / 50
        val moveX = position.x % 50
        val moveY = position.y % 50
        val unwrapped = position.move(direction)

        if (move == (unwrapped.y / 50 * 3 + unwrapped.x / 50) && unwrapped.x >= 0 && unwrapped.y >= 0) return Pair(
            unwrapped,
            direction
        )
        return when (Pair(move, direction)) {
            Pair(1, 0) -> Pair(Vector(0, 3 * 50 + moveX), 90)
            Pair(1, 90) -> Pair(unwrapped, 90)
            Pair(1, 180) -> Pair(unwrapped, 180)
            Pair(1, 270) -> Pair(Vector(0, 3 * 50 - 1 - moveY), 90)
            Pair(2, 0) -> Pair(Vector(moveX, 4 * 50 - 1), 0)
            Pair(2, 90) -> Pair(Vector(2 * 50 - 1, 3 * 50 - 1 - moveY), 270)
            Pair(2, 180) -> Pair(Vector(2 * 50 - 1, 50 + moveX), 270)
            Pair(2, 270) -> Pair(unwrapped, 270)
            Pair(4, 0) -> Pair(unwrapped, 0)
            Pair(4, 90) -> Pair(Vector(2 * 50 + moveY, 50 - 1), 0)
            Pair(4, 180) -> Pair(unwrapped, 180)
            Pair(4, 270) -> Pair(Vector(moveY, 2 * 50), 180)
            Pair(6, 0) -> Pair(Vector(50, 50 + moveX), 90)
            Pair(6, 90) -> Pair(unwrapped, 90)
            Pair(6, 180) -> Pair(unwrapped, 180)
            Pair(6, 270) -> Pair(Vector(50, 50 - 1 - moveY), 90)
            Pair(7, 0) -> Pair(unwrapped, 0)
            Pair(7, 90) -> Pair(Vector(3 * 50 - 1, 50 - 1 - moveY), 270)
            Pair(7, 180) -> Pair(Vector(50 - 1, 3 * 50 + moveX), 270)
            Pair(7, 270) -> Pair(unwrapped, 270)
            Pair(9, 0) -> Pair(unwrapped, 0)
            Pair(9, 90) -> Pair(Vector(50 + moveY, 3 * 50 - 1), 0)
            Pair(9, 180) -> Pair(Vector(2 * 50 + moveX, 0), 180)
            Pair(9, 270) -> Pair(Vector(50 + moveY, 0), 180)
            else -> throw IllegalArgumentException()
        }
    }

    fun parseInput(input: List<String>): Pair<List<String>, Sequence<String>> {
        val map = input[0].lines()
        val path = Regex("[RL]|\\d+").findAll(input[1]).map { it.value }
        return Pair(map, path)
    }

    fun walkPath(
        map: List<String>,
        path: Sequence<String>,
        wrapLogic: (position: Vector, direction: Int) -> Pair<Vector, Int>
    ): Int {
        var currentPosition = Vector(map[0].indexOfFirst { it == '.' }, 0)
        var direction = 90
        path.forEach { step ->
            when (step) {
                "L", "R" -> {
                    direction = floorMod(direction + if (step == "R") 90 else -90, 360)
                    return@forEach
                }
                else -> for (i in 0 until step.toInt()) {
                    val (newPosition, newDirection) = wrapLogic(currentPosition, direction)
                    if (map[newPosition] == '.') {
                        currentPosition = newPosition
                        direction = newDirection
                    } else {
                        break
                    }
                }
            }
        }
        return (currentPosition.y + 1) * 1000 + (currentPosition.x + 1) * 4 + floorMod(direction - 90, 360) / 90
    }

    fun part1(input: List<String>): Int {
        val (map, path) = parseInput(input)
        return walkPath(map, path) { currentPosition, direction -> wrap2D(map, currentPosition, direction) }
    }

    fun part2(input: List<String>): Int {
        val (map, path) = parseInput(input)
        return walkPath(map, path) { currentPosition, direction -> wrap3D(currentPosition, direction) }
    }

    val testInput = File("src", "Day22_test.txt").readText().split("\n\n")
    check(part1(testInput) == 6032)
    check(part2(testInput) == 5031)

    val input = File("src", "Day22.txt").readText().split("\n\n")
    println(part1(input))
    println(part2(input))
}
