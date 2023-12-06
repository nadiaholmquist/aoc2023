package puzzles

import Puzzle

class Day1(input: String) : Puzzle(input) {
	private val digitMap = mapOf(
		"one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5,
		"six" to 6, "seven" to 7, "eight" to 8, "nine" to 9
	) + (1..9).map { it.toString() to it }

	override fun part1() =
		input.lines()
			.sumOf { line ->
				line
					.filter { it.isDigit() }
					.map { it.digitToInt() }
					.let { it.first() * 10 + it.last() }
			}

	override fun part2() =
		input.lines()
			.mapNotNull { line ->
				val first = line.findAnyOf(digitMap.keys)?.second
				val last = line.findLastAnyOf(digitMap.keys)?.second
				if (first == null || last == null) return@mapNotNull null

				(digitMap[first]!! * 10) + digitMap[last]!!
			}.sum()
}