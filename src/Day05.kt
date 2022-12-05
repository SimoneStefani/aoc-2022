fun main() {

    fun numberOfStacks(lines: List<String>) =
        lines.dropWhile { it.contains("[") }.first().trim().split("   ").maxOf { it.toInt() }

    fun loadStacks(input: List<String>): List<ArrayDeque<String>> {
        val numberOfStacks = numberOfStacks(input)
        val stacks = List(numberOfStacks) { ArrayDeque<String>() }

        input.filter { it.contains("[") }.forEach { line ->
            line.chunked(4).mapIndexed { index, s ->
                if (s.isNotBlank()) stacks[index].addLast(s.trim().removeSurrounding("[", "]"))
            }
        }

        return stacks
    }

    fun parseMove(line: String): Triple<Int, Int, Int> {
        val captures = "move (\\d*) from (\\d*) to (\\d*)".toRegex().find(line)!!.groupValues
        val amount = captures[1].toInt()
        val from = captures[2].toInt()
        val to = captures[3].toInt()
        return Triple(amount, from, to)
    }

    fun part1(input: List<String>): String {
        val stacks = loadStacks(input)

        input.filter { it.startsWith("move") }.forEach { line ->
            val (amount, from, to) = parseMove(line)
            repeat(amount) { stacks[to - 1].addFirst(stacks[from - 1].removeFirst()) }
        }

        return stacks.joinToString("") { it.first() }
    }

    fun part2(input: List<String>): String {
        val stacks = loadStacks(input)

        input.filter { it.startsWith("move") }.forEach { line ->
            val (amount, from, to) = parseMove(line)
            stacks[from - 1].subList(0, amount).asReversed()
                .map { stacks[to - 1].addFirst(it) }
                .map { stacks[from - 1].removeFirst() }
        }

        return stacks.joinToString("") { it.first() }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
