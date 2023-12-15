package puzzles

import Puzzle

class Day15(input: String) : Puzzle<Int>(input) {
	fun hash(str: String): Int {
		return str.fold(0) { acc, char ->
			((acc + char.code) * 17) % 256
		}
	}

	override fun part1(): Int {
		return input.split(",").sumOf(::hash)
	}

	val lineRegex = Regex("([a-z]+)([=\\-])(\\d*)")

	override fun part2(): Int {
		val boxes = List(256) { mutableListOf<Pair<String, Int>>() }

		input.split(",").forEach { line ->
			val (_, name, operation, focalLength) = lineRegex.matchEntire(line)?.groupValues ?: return@forEach
			val boxNum = hash(name)

			when (operation) {
				"-" -> boxes[boxNum].removeIf { it.first == name }
				"=" -> {
					val currLens = boxes[boxNum].indexOfFirst { it.first == name }
					val newLens = name to focalLength.toInt()

					if (currLens == -1) boxes[boxNum].add(newLens)
					else boxes[boxNum][currLens] = newLens
				}
			}
		}

		return boxes.flatMapIndexed { boxNum, box ->
			box.mapIndexed { slotNum, lens -> (boxNum + 1) * (slotNum + 1) * lens.second }
		}.sum()
	}
}