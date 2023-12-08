package puzzles

import Puzzle

class Day8(input: String) : Puzzle<Long>(input) {
	val nodes: List<Pair<Int, Int>>
	val directions: List<Int>
	val startingNodes: List<Pair<Int, Int>>
	val endingNodes: List<Pair<Int, Int>>

	init {
		val seen = mutableMapOf<String, Int>()
		val sortedLines = input.lines().drop(2).sorted()
		val startIndices = mutableListOf<Int>()
		val endIndices = mutableListOf<Int>()

		sortedLines.map { it.take(3) }
			.forEachIndexed { index, sym ->
				seen[sym] = index
				if (sym.endsWith('A')) startIndices += index
				if (sym.endsWith('Z')) endIndices += index
			}
		nodes = sortedLines
			.map { it.substring(7..14).split(", ") }
			.map { (left, right) ->
				seen[left]!! to seen[right]!!
			}
		startingNodes = startIndices.map { nodes[it] }
		endingNodes = endIndices.map { nodes[it] }

		directions = input.substringBefore('\n').map { if (it == 'R') 1 else 0 }
	}

	val directionsSeq = sequence {
		while (true) { directions.forEach { yield(it) }}
	}

	fun distance(start: Pair<Int, Int>, ends: List<Pair<Int, Int>> = endingNodes): Long {
		var currNode = start
		var steps = 0L

		directionsSeq.takeWhile {
			steps++
			currNode = if (it == 0) nodes[currNode.first] else nodes[currNode.second]
			currNode !in ends
		}.last()

		return steps
	}

	fun lcm(elements: List<Long>): Long {
		val temp = elements.toMutableList()
		while (true) {
			var minIndex = -1
			var currMin = Long.MAX_VALUE
			var different = 0
			val first = temp[0]
			for (i in 0..temp.lastIndex) {
				if (temp[i] < currMin) {
					currMin = temp[i]
					minIndex = i
				}
				if (temp[i] != first) different++
			}
			if (different == 0) break

			temp[minIndex] += elements[minIndex]
		}
		return temp[0]
	}

	override fun part1() = distance(startingNodes.first(), listOf(endingNodes.last()))
	override fun part2() = lcm(startingNodes.map(::distance))
}