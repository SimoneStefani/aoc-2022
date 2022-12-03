fun main() {
    fun letterToPriority(letter: Char): Int = when (letter) {
        in 'A'..'Z' -> letter.code - 38 // ASCII code of "A" starts at 65
        in 'a'..'z' -> letter.code - 96 // ASCII code of "a" starts at 97
        else -> throw IllegalStateException("Invalid character!")
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (first, second) = line.splitInHalf()
            val commonLetters = first.toSet().intersect(second.toSet())
            if (commonLetters.size != 1) throw IllegalStateException("Too many common characters!")
            letterToPriority(commonLetters.first())
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { lines ->
            val commonLetters = lines[0].toSet().intersect(lines[1].toSet()).intersect(lines[2].toSet())
            if (commonLetters.size != 1) throw IllegalStateException("Too many common characters!")
            letterToPriority(commonLetters.first())
        }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun String.splitInHalf(): Pair<String, String> {
    val mid: Int = this.length / 2
    return Pair(this.substring(0, mid), this.substring(mid))
}
