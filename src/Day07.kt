fun main() {
    class Node(
        val name: String,
        val parent: Node?,
        val children: MutableSet<Node> = mutableSetOf(),
    ) {
        private var itemSize = 0L
        val totalSize: Long get() = itemSize + children.sumOf { it.totalSize }

        fun addNode(dir: Node): Node = this.also { children.add(dir) }
        fun addItem(size: Long): Node = this.also { itemSize += size }
        fun allChildren(): Set<Node> = children + children.flatMap { it.allChildren() }
    }

    fun parseCommands(input: List<String>): Node {
        return Node("/", null).apply {
            input.fold(this) { node, command: String ->
                when {
                    command == "$ cd /" -> node
                    command == "$ ls" -> node
                    command == "$ cd .." -> node.parent!!
                    command.startsWith("$ cd") -> node.children.first { it.name == command.substringAfter("cd ") }
                    command.startsWith("dir") -> node.addNode(Node(command.substringAfter("dir "), node))
                    else -> node.addItem(command.substringBefore(" ").toLong())
                }
            }
        }
    }

    fun part1(input: List<String>): Long {
        return parseCommands(input).allChildren().filter { it.totalSize <= 100_000 }.sumOf { it.totalSize }
    }

    fun part2(input: List<String>): Long {
        val root = parseCommands(input)
        val missingSpace = 30_000_000L - (70_000_000L - root.totalSize)
        return parseCommands(input).allChildren().filter { it.totalSize >= missingSpace }.minOf { it.totalSize }
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95_437L)
    check(part2(testInput) == 24_933_642L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
