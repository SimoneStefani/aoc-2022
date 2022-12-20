fun main() {
    fun decrypt(input: List<Long>, decryptKey: Int = 1, iterations: Int = 1): Long {
        val original = input.mapIndexed { index, n -> index to n * decryptKey }
        val shifted = original.toMutableList()

        repeat(iterations) {
            original.forEach { p ->
                shifted.indexOf(p).also { idx ->
                    shifted.removeAt(idx)
                    shifted.add((idx + p.second).mod(shifted.size), p)
                }
            }
        }

        val mixed = shifted.map(Pair<Int, Long>::second)
        return sequenceOf(1000, 2000, 3000).sumOf { mixed[(mixed.indexOf(0) + it) % mixed.size] }
    }

    fun parseInput(lines: List<String>): List<Long> = lines.map { it.toLong() }

    fun part1(input: List<String>): Long {
        val sequence = parseInput(input)
        return decrypt(sequence)
    }

    fun part2(input: List<String>): Long {
        val sequence = parseInput(input)
        return decrypt(sequence, decryptKey = 811589153, iterations = 10)
    }

    val testInput = readInput("Day20_test")
    check(part1(testInput) == 3L)
    check(part2(testInput) == 1623178306L)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
