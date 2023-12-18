package puzzles

import Puzzle

class Day18(input: String) : Puzzle<Long>(input) {
	fun polygonArea(points: List<Pair<Long, Long>>): Long {
		var sum = 0L
		for (i in 0..<points.lastIndex) {
			sum += (points[i].first * points[i + 1].second) - (points[i+1].first * points[i].second)
		}
		sum += (points.last().first * points.first().second) - (points.first().first * points.last().second)
		return sum/2
	}

	enum class Direction {
		UP, DOWN, LEFT, RIGHT;
		companion object {
			val chars = mapOf('U' to UP, 'D' to DOWN, 'L' to LEFT, 'R' to RIGHT)
			val numbers = mapOf(0 to RIGHT, 1 to DOWN, 2 to LEFT, 3 to UP)
		}
	}

	fun doPart(selector: (String) -> Pair<Long, Direction>): Long {
		var sum = 0L
		val points = input.lines().runningFold(Pair(0L, 0L)) { acc, it ->
			val (amount, direction) = selector(it)
			sum += amount

			when (direction) {
				Direction.UP -> acc.first to acc.second - amount
				Direction.DOWN -> acc.first to acc.second + amount
				Direction.LEFT -> acc.first - amount to acc.second
				Direction.RIGHT -> acc.first + amount to acc.second
			}
		}

		return polygonArea(points) + (sum/2) + 1
	}

	override fun part1() = doPart {
		val (dirPart, amount) = it.split(" ")
		amount.toLong() to (Direction.chars[dirPart.first()] ?: throw IllegalArgumentException())
	}

	override fun part2() = doPart {
		val hex = it.split("#")[1].dropLast(1)
		val amount = hex.take(5).toLong(16)
		val dirDigit = hex.last().digitToInt()
		amount to (Direction.numbers[dirDigit] ?: throw IllegalArgumentException())
	}
}