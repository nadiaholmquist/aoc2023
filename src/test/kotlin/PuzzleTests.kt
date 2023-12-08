import puzzles.*
import kotlin.test.Test

class PuzzleTests {
	@Test fun day1part1() = test<Day1, Int>(142, null)
	@Test fun day1part2() = test<Day1, Int>(null, 281)
	@Test fun day2() = test<Day2, Int>(8, 2286)
	@Test fun day3() = test<Day3, Int>(4361, 467835)
	@Test fun day4() = test<Day4, Int>(13, 30)
	@Test fun day5() = test<Day5, Long>(35, 46)
	@Test fun day6() = test<Day6, Int>(288, 71503)
	@Test fun day7() = test<Day7, Long>(6440, 5905)
	@Test fun day8part1() = test<Day8, Long>(2, null, "day8_1")
	@Test fun day8part2() = test<Day8, Long>(null, 6, "day8_2")
}