import java.io.File
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.fail

fun getTestInput(named: String): String {
	val inputUrl = PuzzleTests::class.java.classLoader.getResource("${named}.txt") ?: fail()
	return File(inputUrl.toURI()).readText().trimEnd()
}

inline fun <reified T : Puzzle<O>, O> test(expected1: O?, expected2: O?, inputFile: String? = null) {
	val kClass = T::class
	val input = if (inputFile == null) {
		val name = kClass.qualifiedName!!
		val day = name.takeLastWhile { it.isDigit() }.toInt()
		val suffix = when {
			expected1 != null && expected2 == null -> "_1"
			expected1 == null && expected2 != null -> "_2"
			else -> ""
		}
		getTestInput("day$day$suffix")
	} else getTestInput(inputFile)

	val puzzle = kClass.constructors.first().call(input)

	if (expected1 != null && expected2 != null) {
		val actual1 = puzzle.part1()
		val actual2 = puzzle.part2()
		println("Part 1:\n  Actual $actual1\n  Expected: $expected1")
		println("Part 2:\n  Actual $actual2\n  Expected: $expected2")

		assertContentEquals(listOf(expected1, expected2), listOf(actual1, actual2))
	} else if (expected1 != null) {
		assertEquals(expected1, puzzle.part1())
	} else {
		assertEquals(expected2, puzzle.part2())
	}
}
