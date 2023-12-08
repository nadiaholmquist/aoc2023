package puzzles

import Puzzle
import utils.intersects
import kotlin.math.min

class Day5(input: String) : Puzzle<Long>(input) {
	class ItemMap(ranges: List<Pair<LongRange, Long>>, val name: String) {
		val sourceRanges: List<LongRange>
		val offsets: List<Long>

		init {
			val sortedRanges = ranges.sortedBy { it.first.first }
			sourceRanges = sortedRanges.map { it.first }
			offsets = sortedRanges.map { it.second }
		}

		fun mapRange(range: LongRange): List<LongRange> {
			val outRanges = mutableListOf<LongRange>()

			var currIndex = sourceRanges.binarySearch {
				if (it.intersects(range)) 0
				else if (range.last < it.first) 1
				else -1
			}

			val numRanges = sourceRanges.count()
			if (currIndex < 0) return listOf(range)

			var remainingRange = range

			while (currIndex < numRanges) {
				val source = sourceRanges[currIndex]
				val offset = offsets[currIndex]

				if (source.first > remainingRange.first) {
					outRanges += remainingRange.first..<source.first
					remainingRange = source.first..remainingRange.last
				}

				val toTake = min(remainingRange.last, source.last)
				outRanges += (remainingRange.first + offset)..(toTake + offset)
				remainingRange = toTake..remainingRange.last

				if (remainingRange.first >= remainingRange.last) {
					break
				}
				currIndex++
			}

			if (remainingRange.first < remainingRange.last)
				outRanges += (remainingRange.first + 1)..remainingRange.last

			return outRanges
		}

		override fun toString(): String {
			return sourceRanges.zip(offsets).joinToString(", ") {(src, dest) ->
				"${src.first} ${src.first + dest} ${src.count()}"
			}
		}

		companion object {
			fun fromString(string: String): ItemMap {
				val numberRegex = Regex("[\\d]+")
				val lines = string.lines()

				val ranges = string.lines().drop(1).map { line ->
					val matches = numberRegex.findAll(line)
					val (destStart, srcStart, count) = matches.map { it.value.toLong() }.toList()

					srcStart..<(srcStart + count) to destStart - srcStart
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

	override fun part1(): Long {
		val mappings = seeds.map {
			itemMaps.fold(it..it) { acc, map ->
				map.mapRange(acc).first()
			}
		}

		return mappings.minBy { it.first }.first
	}

	override fun part2(): Long {
		val seedGroups = seeds.asSequence()
			.chunked(2)
			.map { (first, last) -> first..<(first+last) }

		val result = seedGroups.flatMap {
			itemMaps.fold(listOf(it)) { acc, map ->
				acc.flatMap { map.mapRange(it) }
			}
		}

		return result.toList().minBy { it.first }.first
	}
}