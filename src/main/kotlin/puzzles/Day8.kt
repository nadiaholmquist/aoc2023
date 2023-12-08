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
		fun gcd(a: Long, b: Long): Long {
			var a2 = a
			var b2 = b
			while (b2 != 0L) {
				val temp = b2
				b2 = a2 % b2
				a2 = temp
			}
			return a2
		}
		fun lcmInner(a: Long, b: Long) = a / gcd(a, b) * b

		return elements.reduce(::lcmInner)
	}

	override fun part1() = distance(startingNodes.first(), listOf(endingNodes.last()))
	override fun part2() = lcm(startingNodes.map(::distance))
}
