package puzzles

import Puzzle

class Day4(input: String) : Puzzle(input) {
	data class Card(val number: Int, val winningNumbers: Set<Int>, val haveNumbers: Set<Int>) {
		val matching = winningNumbers.intersect(haveNumbers).count()
		val pointsWorth = 1 shl (matching - 1)
	}

	val cardRegex = Regex("^Card +(\\d+):([\\d ]+)\\|([\\d ]+)")
	val numberRegex = Regex("[\\d]+")
	val cards = input.lines().asSequence()
		.mapNotNull {
			val (_, num, winningStr, haveStr) = cardRegex.matchEntire(it)?.groupValues ?: return@mapNotNull null
			val winning = numberRegex.findAll(winningStr).map { it.value.toInt() }
			val have = numberRegex.findAll(haveStr).map { it.value.toInt() }
			Card(num.toInt(), winning.toSet(), have.toSet())
		}.toList()

	override fun part1() =
		cards.sumOf { it.pointsWorth }

	override fun part2(): Int {
		val cardAmounts = cards.associateWithTo(mutableMapOf()) { 1 }

		cards.forEachIndexed { index, card ->
			cards.drop(index + 1).take(card.matching).forEach { subCard ->
				val subAmount = cardAmounts[subCard]!!
				cardAmounts[subCard] = subAmount + cardAmounts[card]!!
			}
		}

		return cardAmounts.values.sum()
	}
}