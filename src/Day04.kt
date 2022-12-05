fun main() {


    fun extractRanges(line: String): Pair<IntRange, IntRange> {
        val (first, second) = line.split(",")
        val (fStart, fEnd) = first.split("-").map { it.toInt() }
        val (sStart, sEnd) = second.split("-").map { it.toInt() }
        return Pair(IntRange(fStart, fEnd), IntRange(sStart, sEnd))
    }

    fun part1(input: List<String>): Int {
        return input.count { line ->
            val (firstRange, secondRange) = extractRanges(line)
            firstRange.fullyOverlapsWith(secondRange) || secondRange.fullyOverlapsWith(firstRange)
        }
    }

    fun part2(input: List<String>): Int {
        return input.count { line ->
            val (firstRange, secondRange) = extractRanges(line)
            firstRange.overlapsWith(secondRange)
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

fun IntRange.fullyOverlapsWith(other: IntRange) = this.first <= other.first && this.last >= other.last

fun IntRange.overlapsWith(other: IntRange) = this.intersect(other).isNotEmpty()