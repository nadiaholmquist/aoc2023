package puzzles

import Puzzle

class Day14(input: String) : Puzzle<Int>(input) {
	fun getMap() = input.lines().map {
		it.toMutableList()
	}

	fun getRocks(map: List<List<Char>>) = map.flatMapIndexed { y, line ->
		line.mapIndexedNotNull { x, char ->
			if (char == 'O') x to y else null
		}
	}

	fun moveRock(map: List<MutableList<Char>>, rockX: Int, rockY: Int, dirX: Int, dirY: Int): Pair<Int, Int> {
		var posX = rockX
		var posY = rockY
		var nextX: Int
		var nextY: Int
		var nextChar: Char
		while (true) {
			nextX = posX + dirX
			nextY = posY + dirY
			nextChar = map.getOrNull(nextY)?.getOrNull(nextX) ?: break
			if (nextChar != '.') break
			posX = nextX
			posY = nextY
		}
		map[rockY][rockX] = '.'
		map[posY][posX] = 'O'
		return posX to posY
	}

	override fun part1(): Int {
		val map = getMap()
		val rocks = getRocks(map)

		return rocks.map { moveRock(map, it.first, it.second, 0, -1) }.sumOf { map.size - it.second }
	}

	override fun part2(): Int {
		val map = getMap()
		val rocks = getRocks(map).toMutableList()

		val width = map.first().size
		val directions = listOf(0 to -1, -1 to 0, 0 to 1, 1 to 0)

		val lastMaps = mutableListOf(map.hashCode())
		var firstMatch: Long = 0
		var iteration = 0L
		val totalIterations = 1000000000L
		while (iteration++ < totalIterations) {
			for (dir in directions) {
				if (dir.first == -1 || dir.second == -1) {
					for (i in rocks.indices) {
						val newRock = moveRock(map, rocks[i].first, rocks[i].second, dir.first, dir.second)
						rocks[i] = newRock
					}
				} else {
					for (i in rocks.indices.reversed()) {
						val newRock = moveRock(map, rocks[i].first, rocks[i].second, dir.first, dir.second)
						rocks[i] = newRock
					}
				}
				if (dir.second != 0) {
					var index = 0

					map.forEachIndexed { y, line ->
						line.forEachIndexed { x, char ->
							if (char == 'O') rocks[index++] = x to y
						}
					}
				}
			}

			if (map.hashCode() in lastMaps) {
				if (firstMatch == 0L) {
					firstMatch = iteration
				} else {
					val cycleLength = iteration - firstMatch
					val numCycles = ((totalIterations - firstMatch) / cycleLength) - 1
					iteration += cycleLength * numCycles
				}
				lastMaps.clear()
			}
			lastMaps.add(map.hashCode())
		}

		return rocks.sumOf { map.size - it.second }
	}
}