fun main() {
    fun part1(input: List<String>): Int {
        return (input + "").fold(Pair(0, 0)) { acc, curr ->  // Pair(max so far, current elf)
            if (curr.isBlank()) {
                Pair(maxOf(acc.first, acc.second), 0) // is an empty line
            } else {
                Pair(acc.first, acc.second + curr.toInt()) // is a number
            }
        }.first
    }

    fun part2(input: List<String>): Int {
        var partial = 0
        return (input + "").mapNotNull { curr ->
            if (curr.isBlank()) {
                return@mapNotNull partial.also { partial = 0 } // is an empty line
            } else {
                partial += curr.toInt() // is a number
                null
            }
        }.sortedDescending().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
