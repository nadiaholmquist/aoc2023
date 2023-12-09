package puzzles

import Puzzle

class Day9(input: String) : Puzzle<Int>(input) {
	val numberRegex = Regex("-?\\d+")

	val  sequences = input.lines()
		.asSequence()
		.map { numberRegex.findAll(it).map { it.value.toInt() }.toList() }
		.map {
			buildList {
				var seq = it
				while (!seq.all { it == 0 }) {
					add(seq)
					seq = seq.zipWithNext { a, b -> b - a }
				}
			}
		}

	fun doPart(part: Int) =
		sequences.sumOf { seqs ->
			seqs.foldRight(0.toInt()) { it, acc ->
				if (part == 1) acc + (it.last())
				else (it.first()) - acc
			}
		}

	override fun part1() = doPart(1)
	override fun part2() = doPart(2)
}