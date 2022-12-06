fun main() {
    fun detectFirstUniqueSubstring(signal: String, n: Int): Int {
        var markerIndex = n
        for (i in n until signal.length - 1) {
            if (signal.substring(i - n, i).toSet().size == n) {
                markerIndex = i
                break
            }
        }
        return markerIndex
    }

    fun part1(input: List<String>): Int {
        return detectFirstUniqueSubstring(input.first(), 4)
    }

    fun part2(input: List<String>): Int {
        return detectFirstUniqueSubstring(input.first(), 14)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
