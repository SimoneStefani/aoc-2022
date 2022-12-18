fun main() {
    data class Point(val x: Int, val y: Int, val z: Int) {
        fun neighbours() = setOf(
            Point(x + 1, y, z), // right
            Point(x - 1, y, z), // left
            Point(x, y + 1, z), // up
            Point(x, y - 1, z), // down
            Point(x, y, z + 1), // front
            Point(x, y, z - 1), // back
        )
    }

    fun computeAirBoundary(points: List<Point>, start: Point, max: Point): Set<Point> {
        var toVisit = setOf(start)
        val visited = mutableSetOf(start)
        while (toVisit.isNotEmpty()) {
            toVisit = toVisit
                .flatMap { it.neighbours() - points.toSet() }
                .minus(visited)
                .filter { it.x in start.x..max.x && it.y in start.y..max.y && it.z in start.z..max.z }
                .toSet()
            visited += toVisit
        }
        return visited
    }

    fun parseInput(lines: List<String>) = lines
        .map { it.split(",") }
        .map { (x, y, z) -> Point(x.toInt(), y.toInt(), z.toInt()) }

    fun part1(input: List<String>): Int {
        val points = parseInput(input)
        return points.sumOf { it.neighbours().count { n -> n !in points } }
    }

    fun part2(input: List<String>): Int {
        val points = parseInput(input)
        val start = Point(points.minOf { it.x } - 1, points.minOf { it.y } - 1, points.minOf { it.z } - 1)
        val max = Point(points.maxOf { it.x } + 1, points.maxOf { it.y } + 1, points.maxOf { it.z } + 1)
        val airBoundary = computeAirBoundary(points, start, max)
        return points.sumOf { it.neighbours().count { n -> n in airBoundary } }
    }

    val testInput = readInput("Day18_test")
    check(part1(testInput) == 64)
    check(part2(testInput) == 58)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
