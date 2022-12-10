fun main() {
    class Forest(private val rows: List<List<Int>>) {
        private fun isVisible(r: Int, c: Int): Boolean =
            (0 until c).all { rows[r][it] < rows[r][c] }
                    || (c + 1 until rows[0].size).all { rows[r][it] < rows[r][c] }
                    || (0 until r).all { rows[it][c] < rows[r][c] }
                    || (r + 1 until rows.size).all { rows[it][c] < rows[r][c] }

        private fun score(r: Int, c: Int): Int =
            (c - 1 downTo 0).takeWhile { rows[r][it] >= rows[r][c] }.size *
                    (c + 1 until rows[0].size).takeWhile { rows[r][it] >= rows[r][c] }.size *
                    (r - 1 downTo 0).takeWhile { rows[it][c] >= rows[r][c] }.size *
                    (r + 1 until rows.size).takeWhile { rows[it][c] >= rows[r][c] }.size

        fun maxScenicScore(): Int =
            (1 until rows.size - 1).maxOf { r -> (1 until rows[0].size - 1).maxOf { c -> score(r, c) } }

        fun countVisible(): Int =
            (rows.indices).sumOf { r -> (0 until rows[r].size).count { c -> isVisible(r, c) } }
    }

    fun loadForest(lines: List<String>): Forest =
        Forest(lines.map { line -> line.map { it.code - '0'.code }.toList() }.toList())


    fun part1(input: List<String>): Int {
        return loadForest(input).countVisible()
    }

    fun part2(input: List<String>): Int {
        return loadForest(input).maxScenicScore()
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
