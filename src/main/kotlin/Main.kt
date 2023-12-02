import puzzles.*

fun runPuzzle(day: Int): Pair<Int, Int>? {
    val puzzle: Puzzle

    try {
        puzzle = Class.forName("puzzles.Day$day").constructors[0].newInstance() as Puzzle
    } catch (e: Exception) {
        return null
    }

    return puzzle.part1() to puzzle.part2()
}
fun main(args: Array<String>) {
    if (args.isEmpty()) {
        for (i in 1..25) {
            val result = runPuzzle(i) ?: break

            println("--- DAY $i ---")
            println("Part 1: ${result.first}")
            println("Part 2: ${result.second}")
        }
    } else {
        val dayNum = args[0].toIntOrNull()
        if (dayNum == null ||  dayNum !in 1..25) {
            println("Specify a day number between 1 and 25")
            return
        }

        val result = runPuzzle(dayNum)
        if (result == null) {
            println("Day is not implemented yet.")
            return
        }

        println("Part 1: ${result.first}")
        println("Part 2: ${result.second}")
    }
}