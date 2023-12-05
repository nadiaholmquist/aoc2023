package puzzles

import Puzzle
import kotlin.concurrent.thread

class Day5(input: String) : Puzzle(input) {
	class ItemMap(ranges: List<Pair<LongRange, LongRange>>, val name: String) {
		val sourceRanges: List<LongRange>
		val destRanges: List<LongRange>

		init {
			val sortedRanges = ranges.sortedBy { it.first.first }
			sourceRanges = sortedRanges.map { it.first }
			destRanges = sortedRanges.map { it.second }
		}

		fun findMapping(entry: Long): Long {
			val foundIndex = sourceRanges.binarySearch {
				if (entry in it) 0
				else if (entry < it.first) 1
				else -1
			}

			if (foundIndex < 0) return entry

			val inSource = sourceRanges[foundIndex]
			val inDest = destRanges[foundIndex]

			return inDest.first + (entry - inSource.first)
		}

		override fun toString(): String {
			return sourceRanges.zip(destRanges).joinToString(", ") {(src, dest) ->
				"${src.first} ${dest.first} ${src.count()}"
			}
		}

		companion object {
			fun fromString(string: String): ItemMap {
				val numberRegex = Regex("[\\d]+")
				val lines = string.lines()

				val ranges = string.lines().drop(1).map { line ->
					val matches = numberRegex.findAll(line)
					val (destStart, srcStart, count) = matches.map { it.value.toLong() }.toList()

					srcStart..<(srcStart + count) to destStart..<(destStart + count)
				}

				return ItemMap(ranges, lines.first())
			}
		}
	}

	val seeds = input.lines()
		.first().removePrefix("seeds: ")
		.split(" ").map { it.toLong() }

	val itemMaps = input.split("\n\n")
		.drop(1)
		.map(ItemMap::fromString)

	override fun part1(): Int {
		val mappings = seeds.map {
			itemMaps.fold(it) { acc, map ->
				map.findMapping(acc)
			}
		}

		return mappings.min().toInt()
	}

	override fun part2(): Int {
		val seedGroups = seeds.asSequence()
			.chunked(2)
			.map { (first, last) -> first..<(first+last) }

		val result = LongArray(seedGroups.count())
		seedGroups.toList().mapIndexed { index, it ->
			 thread {
				  result[index] = it.minOf {
					  itemMaps.fold(it) { acc, map ->
						   map.findMapping(acc)
					  }
				  }
			 }
		}.forEach { it.join() }

		return result.min().toInt()
	}
}