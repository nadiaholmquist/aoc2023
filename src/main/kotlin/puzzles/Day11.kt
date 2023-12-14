package puzzles

import Puzzle
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class Day11(input: String): Puzzle<Long>(input) {
	fun parseInput(): Pair<List<Pair<Long, Long>>, Pair<List<Int>, List<Int>>> {
		val lines = input.lines()

		val emptyRows = lines.withIndex()
			.filterNot { '#' in it.value }
			.map { it.index }

		val emptyColumns = lines.first().indices.filter {x ->
			lines.indices.all { y -> lines[y][x] != '#' }
		}

		val points = lines.flatMapIndexed { y, row ->
			row.mapIndexedNotNull { x, char ->
				if (char == '#') x.toLong() to y.toLong() else null
			}
		}

		return points to (emptyRows to emptyColumns)
	}

	fun sumDistances(distanceModifier: Int = 2): Long {
		val (points, empties) = parseInput()
		val (emptyRows, emptyColumns) = empties

		return points.mapIndexed { index, currPoint ->
			points.drop(index+1).sumOf { otherPoint ->
				val minX = min(currPoint.first, otherPoint.first)
				var maxX = max(currPoint.first, otherPoint.first)
				val minY = min(currPoint.second, otherPoint.second)
				var maxY = max(currPoint.second, otherPoint.second)

				maxX += emptyColumns.count {it in minX..maxX } * (distanceModifier - 1)
				maxY += emptyRows.count { it in minY..maxY } * (distanceModifier - 1)

				abs(minX - maxX) + abs(minY - maxY)
			}
		}.sum()
	}

	override fun part1(): Long = sumDistances()
	override fun part2(): Long = sumDistances(1000000)
}
