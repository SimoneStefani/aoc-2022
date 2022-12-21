// FIXME improve name with better namespacing
data class Monkey21(
    val name: String,
    val number: Long? = null,
    val left: String? = null,
    val operator: String? = null,
    val right: String? = null,
) {
    fun resolve(left: Long, right: Long): Long = when (operator) {
        "+" -> left + right
        "-" -> left - right
        "*" -> left * right
        "/" -> left / right
        else -> -1
    }

    fun derive(value: Long, other: Long, left: Boolean): Long = when (operator) {
        "+" -> value - other
        "-" -> if (left) value + other else other - value
        "*" -> value / other
        "/" -> if (left) value * other else other / value
        else -> -1
    }

    companion object {
        fun fromLine(line: String): Monkey21 {
            val parts = line.split(" ")
            val name = parts[0].trim(':')
            return if (parts.size == 2) {
                Monkey21(name, parts[1].toLong())
            } else {
                Monkey21(name, left = parts[1], operator = parts[2], right = parts[3])
            }
        }
    }
}

class Troop(private val monkeys: Map<String, Monkey21>, private val tree: HashMap<String, String>) {
    fun yelledByRoot(name: String): Long {
        val monkey = monkeys[name]!!
        return monkey.number ?: monkey.resolve(yelledByRoot(monkey.left!!), yelledByRoot(monkey.right!!))
    }

    fun yelledByHuman(name: String): Long {
        val parent = tree[name]!!
        val parentMonkey = monkeys[parent]!!
        val left = parentMonkey.left!! == name
        val other = if (left) yelledByRoot(parentMonkey.right!!) else yelledByRoot(parentMonkey.left!!)
        return if (parent == "root") other else parentMonkey.derive(yelledByHuman(parent), other, left)
    }

    companion object {
        fun fromLines(lines: List<String>): Troop {
            val tree = HashMap<String, String>()
            return lines.map { line ->
                Monkey21.fromLine(line).also { monkey ->
                    monkey.right?.let { tree[it] = monkey.name }
                    monkey.left?.let { tree[it] = monkey.name }
                }
            }.associateBy { it.name }.let { Troop(it, tree) }
        }
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        return Troop.fromLines(input).yelledByRoot("root")
    }

    fun part2(input: List<String>): Long {
        return Troop.fromLines(input).yelledByHuman("humn")
    }

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 152L)
    check(part2(testInput) == 301L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
