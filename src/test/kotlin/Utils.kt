import java.io.File
import kotlin.test.assertEquals
import kotlin.test.fail

fun test(day: Int, part: Int, expected: Int, inputFile: String? = null) {
	val constructor = Class.forName("puzzles.Day$day").kotlin.constructors.first()
	val inputName = inputFile ?: "day${day}_${part}"

	val inputUrl = PuzzleTests::class.java.classLoader.getResource("${inputName}.txt") ?: fail()
	val input = File(inputUrl.toURI()).readText().trimEnd()

	val puzzle = constructor.call(input) as Puzzle
	val result = if (part == 1) puzzle.part1() else puzzle.part2()
	if (result != expected) println("Input:\n$input")
	assertEquals(result, expected)
}

fun testPart1(day: Int, expected: Int, inputFile: String? = null) =
	test(day, 1, expected, inputFile)

fun testPart2(day: Int, expected: Int, inputFile: String? = null) =
	test(day, 2, expected, inputFile)

fun testBoth(day: Int, expected1: Int, expected2: Int, inputFile: String? = null) {
	val constructor = Class.forName("puzzles.Day$day").kotlin.constructors.first()
	val inputName = inputFile ?: "day${day}"

	val inputUrl = PuzzleTests::class.java.classLoader.getResource("${inputName}.txt") ?: fail()
	val input = File(inputUrl.toURI()).readText().trimEnd()

	val puzzle = constructor.call(input) as Puzzle
	val result1 = puzzle.part1()
	val result2 = puzzle.part2()

	if (result1 != expected1 || result2 != expected2) println("Input:\n$input")

	assertEquals(result1, expected1)
	assertEquals(result2, expected2)
}
