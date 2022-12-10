import kotlin.math.abs
import kotlin.math.sign

fun main() {
    data class Point(val x: Int, val y: Int) {
        val up get() = Point(x, y + 1)
        val down get() = Point(x, y - 1)
        val left get() = Point(x - 1, y)
        val right get() = Point(x + 1, y)

        private fun isAdjacent(other: Point) = other != this && abs(this.x - other.x) <= 1 && abs(y - other.y) <= 1

        fun tailStep(tail: Point): Point =
            if (isAdjacent(tail)) tail
            else Point(tail.x + (x - tail.x).sign, tail.y + (y - tail.y).sign)
    }

    class Grid(knotsCount: Int = 2) {
        private var knots = MutableList(knotsCount) { Point(0, 0) }
        val path = mutableListOf(Point(0, 0))

        fun step(c: Char) {
            when (c) {
                'R' -> knots[0] = knots[0].right
                'L' -> knots[0] = knots[0].left
                'U' -> knots[0] = knots[0].up
                'D' -> knots[0] = knots[0].down
            }

            for (i in 1 until knots.size)
                knots[i] = knots[i - 1].tailStep(knots[i])

            if (path.last() != knots.last()) path.add(knots.last())
        }
    }

    fun simulateRope(input: List<String>, knotsCount: Int = 2): Int {
        return Grid(knotsCount).also { grid ->
            input.forEach { motion -> repeat(motion.substringAfter(" ").toInt()) { grid.step(motion[0]) } }
        }.path.toSet().size
    }

    fun part1(input: List<String>): Int {
        return simulateRope(input, 2)
    }

    fun part2(input: List<String>): Int {
        return simulateRope(input, 10)
    }

    val test2 = """
        R 5
        U 8
        L 8
        D 3
        R 17
        D 10
        L 25
        U 20
    """.trimIndent().lines()
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    check(part2(test2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
