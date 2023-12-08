package puzzles

import Puzzle

class Day7(input: String) : Puzzle<Long>(input) {
	enum class Card {
		JOKER, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
		TEN, JACK, QUEEN, KING, ACE;

		companion object {
			private val numList = listOf(TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE)
			fun fromChar(char: Char) =
				when (char) {
					in '2'..'9' -> numList[char.digitToInt() - 2]
					'T' -> TEN; 'J' -> JACK; 'Q' -> QUEEN; 'K' -> KING; 'A' -> ACE
					else -> throw IllegalArgumentException("'$char' is not a valid character for a card.")
				}
		}
	}

	enum class HandType {
		HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_KIND, FULL_HOUSE, FOUR_OF_KIND, FIVE_OF_KIND
	}

	fun handStrength(hand: List<Card>, handleJokers: Boolean = false): Long {
		if (hand.count() != 5) throw IllegalArgumentException("Incorrect number of cards")

		val usedHand =
			if (handleJokers) hand.map { if (it == Card.JACK) Card.JOKER else it }
			else hand

		val groups = usedHand
			.filterNot { it == Card.JOKER }
			.groupBy { it }.values
			.map { it.count() }
			.sortedDescending()
			.toMutableList()

		if (handleJokers) {
			when (val jokers = usedHand.count { it == Card.JOKER }) {
				5 -> groups += 5
				0 -> {}
				else -> groups[0] += jokers
			}
		}

		val count = groups.count()
		val type = when {
				count == 1 -> HandType.FIVE_OF_KIND
				count == 2 && groups[0] == 4 -> HandType.FOUR_OF_KIND
				count == 2 && groups[0] == 3 -> HandType.FULL_HOUSE
				count == 3 && groups[0] == 3 -> HandType.THREE_OF_KIND
				count == 3 && groups[0] == 2 && groups[1] == 2 -> HandType.TWO_PAIR
				count == 4 -> HandType.ONE_PAIR
				count == 5 -> HandType.HIGH_CARD
				else -> throw Exception("This hand should be impossible")
			}

		return usedHand.fold(type.ordinal.toLong()) { acc, card ->
			(acc shl 4) or card.ordinal.toLong()
		}
	}

	val hands = input.lines().map {
		val parts = it.split(" ")
		parts[0].map { Card.fromChar(it) }to parts[1].toLong()
	}

	fun doPart(part: Int) = hands
		.map { handStrength(it.first, part == 2) to it.second }
		.sortedBy { it.first }
		.foldIndexed(0L) { index, acc, (_, bid) -> acc + bid * (index + 1) }

	override fun part1() = doPart(1)
	override fun part2() = doPart(2)
}