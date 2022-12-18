import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Point(val x: Long, val y: Long) {
        fun manhattan(other: Point): Long = (x - other.x).absoluteValue + (y - other.y).absoluteValue
        fun tuningFreq() = x * 4_000_000L + y
    }

    data class Sensor(val coords: Point, val beacon: Point) {
        val radius = coords.manhattan(beacon)
        fun contains(p: Point): Boolean = coords.manhattan(p) <= radius
        fun skipHorizontally(currentY: Long) = coords.x + radius - (coords.y - currentY).absoluteValue + 1
    }

    fun parseLine(line: String): Sensor {
        val (sx, sy, bx, by) = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)")
            .matchEntire(line)?.destructured ?: throw IllegalArgumentException("Can't parse line: $line")
        return Sensor(Point(sx.toLong(), sy.toLong()), Point(bx.toLong(), by.toLong()))
    }

    fun part1(input: List<String>, atY: Long): Int {
        var minX = 0L
        var maxX = 0L
        val sensors = input.map { line ->
            parseLine(line).also {
                minX = min(minX, it.coords.x - it.radius)
                maxX = max(maxX, it.coords.x + it.radius)
            }
        }
        return (minX - 1..maxX + 1).count { x -> sensors.any { it.contains(Point(x, atY)) } } - 1
    }

    fun part2(input: List<String>, max: Long): Long {
        val sensors = input.map(::parseLine)
        (0..max).forEach { y ->
            var x = 0L
            while (x <= max) {
                val p = Point(x, y)
                val sensor = sensors.find { it.contains(p) } ?: return p.tuningFreq()
                x = sensor.skipHorizontally(y)
            }
        }
        return 0L
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2_000_000))
    println(part2(input, 4_000_000L))
}
