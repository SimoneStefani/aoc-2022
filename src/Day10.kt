fun main() {
    class Cpu {
        var cumulativeSignalStrength = 0
        private var cycle = 0
        private var register: Int = 1
        private var image = ""

        fun nextCycle() {
            cycle += 1
            image += if ((image.length % 40) in register - 1..register + 1) "#" else "."
            if ((cycle - 20) % 40 == 0)
                cumulativeSignalStrength += cycle * register
        }

        fun add(a: Int): Unit = nextCycle().also { nextCycle() }.also { register += a }

        fun formatImage(): String = image.chunked(40).joinToString("\n") { it }
    }

    fun simulateCpu(input: List<String>): Cpu {
        val cpu = Cpu()
        input.forEach { instruction ->
            when {
                instruction.startsWith("noop") -> cpu.nextCycle()
                instruction.startsWith("addx") -> cpu.add(instruction.substringAfter("addx ").toInt())
            }
        }
        return cpu
    }

    fun part1(input: List<String>): Int {
        return simulateCpu(input).cumulativeSignalStrength
    }

    fun part2(input: List<String>): String {
        return simulateCpu(input).formatImage()
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(
        part2(testInput) == """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
        """.trimIndent()
    )

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
