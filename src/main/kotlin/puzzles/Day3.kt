package puzzles

import Puzzle

class Day3 : Puzzle(3) {
	interface Part
	data class Symbol(val x: Int, val y: Int, val symbol: Char) : Part
	data class Number(val x: Int, val y: Int, val value: Int, val digits: Int): Part {
		val range = x..<(x + digits)
	}

	val partsRegex = Regex("[0-9]+|[^.]")
	val parts = input.lines().flatMapIndexed { y, line ->
		partsRegex.findAll(line).map {
			when (it.value.toIntOrNull() != null) {
				 true -> Number(it.range.first, y, it.value.toInt(), it.range.count())
				 false -> Symbol(it.range.first, y, it.value[0])
			}
		}
	}

	val numbers = parts.filterIsInstance<Number>().groupBy { it.y }
	val symbols = parts.filterIsInstance<Symbol>()

	val adjacentNumbers = symbols.asSequence()
		.map { (x, y, symbol) ->
			val positions = listOf(
				(x-1)..(x+1) to y - 1,
				(x-1)..(x+1) to y,
				(x-1)..(x+1) to y + 1
			)

			symbol to positions.flatMap { (range, y) ->
				numbers[y]?.filter { number ->
					number.range.intersect(range).isNotEmpty()
				} ?: emptyList()
			}
		}

	override fun part1() = adjacentNumbers
		.flatMap { it.second }
		.sumOf { it.value }

	override fun part2() = adjacentNumbers
		.filter { (symbol, numbers) -> symbol == '*' && numbers.count() > 1 }
		.map { it.second.map { it.value }.reduce(Int::times) }
		.sum()
}