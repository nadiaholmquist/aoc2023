package puzzles

import Puzzle

class Day16(input: String) : Puzzle<Int>(input) {
	fun doBeam(
		map: List<List<Char>>,
		energized: List<MutableList<Boolean>>,
		evaluated: HashSet<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
		start: Pair<Int, Int>, startDirection: Pair<Int, Int>
	) {
		if (start to startDirection in evaluated) return
		evaluated.add(start to startDirection)

		var pos = start
		var direction = startDirection
		val usedMirrors = hashSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

		while (true) {
			if (pos.first !in map[0].indices || pos.second !in map.indices) break
			energized[pos.second][pos.first] = true
			when (val tile = map[pos.second][pos.first]) {
				'.' -> {}
				'/', '\\' -> {
					if (tile == '/') direction = -direction.second to -direction.first
					else direction = direction.second to direction.first
					if (pos to direction in usedMirrors) break
					usedMirrors.add(pos to direction)
				}
				'-', '|' -> {
					if (tile == '-' && direction.second != 0) {
						doBeam(map, energized, evaluated, pos.first - 1 to pos.second, -1 to 0)
						doBeam(map, energized, evaluated, pos.first + 1 to pos.second, 1 to 0)
						break
					} else if (tile == '|' && direction.first != 0) {
						doBeam(map, energized, evaluated, pos.first to pos.second - 1, 0 to -1)
						doBeam(map, energized, evaluated, pos.first to pos.second + 1, 0 to 1)
						break
					}
				}
			}
			pos = pos.first + direction.first to pos.second + direction.second
		}
	}

	val map = input.lines().map { it.toList() }

	fun assessBeamPosition(start: Pair<Int, Int>, direction: Pair<Int, Int>): Int {
		val energized = map.map { it.map { false }.toMutableList() }
		val evaluated = hashSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
		doBeam(map, energized, evaluated, start, direction)
		return energized.sumOf { it.count { it == true } }
	}

	override fun part1(): Int {
		return assessBeamPosition(0 to 0, 1 to 0)
	}

	override fun part2(): Int {
		return listOf(
			(0..map.first().lastIndex).maxOf { assessBeamPosition(it to 0, 0 to 1) },
			(0..map.first().lastIndex).maxOf { assessBeamPosition(it to map.lastIndex, 0 to -1) },
			(0..map.lastIndex).maxOf { assessBeamPosition(0 to it, 1 to 0) },
			(0..map.lastIndex).maxOf { assessBeamPosition(map.first().lastIndex to it, -1 to 0) }
		).max()
	}
}