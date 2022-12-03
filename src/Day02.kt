fun main() {
    fun computeScoreFromGame(game: String) = when (game) {
        "A X" -> 1 + 3 // rock x rock
        "A Y" -> 2 + 6 // rock x paper
        "A Z" -> 3 + 0 // rock x scissors
        "B X" -> 1 + 0 // paper x rock
        "B Y" -> 2 + 3 // paper x paper
        "B Z" -> 3 + 6 // paper x scissors
        "C X" -> 1 + 6 // scissors x rock
        "C Y" -> 2 + 0 // scissors x paper
        "C Z" -> 3 + 3 // scissors x scissors
        else -> throw IllegalArgumentException("Invalid game $game!")
    }

    fun computeScoreFromOutcome(game: String) = when (game) {
        "A X" -> 0 + 3 // lose -> rock x scissors
        "A Y" -> 3 + 1 // draw -> rock x rock
        "A Z" -> 6 + 2 // win -> rock x paper
        "B X" -> 0 + 1 // lose -> paper x rock
        "B Y" -> 3 + 2 // draw ->paper x paper
        "B Z" -> 6 + 3 // win -> paper x scissors
        "C X" -> 0 + 2 // lose -> scissors x paper
        "C Y" -> 3 + 3 // draw -> scissors x scissors
        "C Z" -> 6 + 1 // win -> scissors x rock
        else -> throw IllegalArgumentException("Invalid game $game!")
    }

    fun part1(input: List<String>): Int = input.sumOf { computeScoreFromGame(it) }

    fun part2(input: List<String>): Int = input.sumOf { computeScoreFromOutcome(it) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
