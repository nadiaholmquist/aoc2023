import puzzles.*
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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
	@Test fun day9() = test<Day9, Int>(114, 2, "day9")
	@Test fun day10() = test<Day10, Int>(4, null, "day10_1")
	@Test fun day10part2() = test<Day10, Int>(null, 8, "day10_2")
	@Test fun day11() = test<Day11, Long>(374, 82000210, "day11")
	@Test fun day14() = test<Day14, Int>(136, 64, "day14")
	@Test fun day15() = test<Day15, Int>(1320, 145, "day15")
	@Test fun day16() = test<Day16, Int>(46, 51, "day16")
}