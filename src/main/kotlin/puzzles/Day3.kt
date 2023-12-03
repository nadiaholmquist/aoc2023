package puzzles

import Puzzle
import kotlin.math.max
import kotlin.math.min

class Day3 : Puzzle(3) {
	val numberRegex = Regex("[0-9]+")

	inner class Number(val x: Int, val y: Int, val value: Int, val digits: Int) {
		val range = x..<(x + digits)

		fun isPartNumber(): Boolean {
			val lines = input.lines()
			val lineCount = lines.count()
			val columnCount = lines[0].count()
			val xRange = max(0, range.first - 1)..min(range.last + 1, columnCount - 1)
			val yRange = max(0, y - 1)..min(y + 1, lineCount - 1)

			return yRange.any { y ->
				val line = lines[y]
				xRange.any {
					val char = line[it]
					!(char in '0'..'9'|| char == '.')
				}
			}
		}
	}

	fun scanNumbers(): List<Number> =
		input.lines().flatMapIndexed { y, line ->
			numberRegex.findAll(line).map { match ->
				Number(match.range.first, y, match.value.toInt(), match.range.count())
			}
		}

	fun scanGears(): List<Pair<Int, Int>> =
		input.lines().flatMapIndexed { y, line ->
			line.indices.filter {
				line[it] == '*'
			}.map { it to y }
		}

	val numbers = scanNumbers()
	val gears = scanGears()

	override fun part1() =
		numbers.filter { it.isPartNumber() }.sumOf { it.value }

	override fun part2(): Int {
		val groupedNumbers = numbers.groupBy { it.y }

		return gears.asSequence()
			.map { (x, y) ->
				val positions = listOf(
					(x-1)..(x+1) to y - 1,
					(x-1)..(x+1) to y,
					(x-1)..(x+1) to y + 1
				)

				positions.flatMap { (range, y) ->
					groupedNumbers[y]?.filter { number ->
						number.range.intersect(range).isNotEmpty()
					} ?: emptyList()
				}
			}
			.filter { it.count() > 1 }
			.map { it.map { it.value }.reduce(Int::times) }
			.sum()
	}
}