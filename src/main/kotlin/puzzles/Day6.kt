package puzzles

import Puzzle
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

class Day6(input: String) : Puzzle(input) {
	val races: List<Pair<Long, Long>>
	val singleRace: Pair<Long, Long>

	init {
		val lines = input.lines()
		val numberRegex = Regex("([\\d+]+)")
		val timeLine = lines[0].removePrefix("Time:")
		val distanceLine = lines[1].removePrefix("Distance:")

		val times = numberRegex.findAll(timeLine).map { it.value }
		val distances = numberRegex.findAll(distanceLine).map { it.value }
		races = times.map(String::toLong).zip(distances.map(String::toLong)).toList()
		singleRace = times.joinToString("").toLong() to distances.joinToString("").toLong()
	}

	fun calcHoldTimes(time: Double, distance: Double): Pair<Double, Double> {
		val t1 = (1.0 / 2) * (time - sqrt(time.pow(2) - (4.0 * distance)))
		val t2 = (1.0 / 2) * (sqrt(time.pow(2) - (4.0 * distance)) + time)
		return Pair(t1, t2)
	}

	fun determineWinningTimes(race: Pair<Long, Long>): Int {
		val (time, distance) = race
		val (h1, h2) = calcHoldTimes(time.toDouble(), distance.toDouble())
		val range = h1..<h2
		val winningIntegers = ((floor(range.start) + 1).toInt()..(ceil(range.endExclusive) - 1).toInt())

		return winningIntegers.last - winningIntegers.first + 1
	}

	override fun part1() =
		races.map(::determineWinningTimes).reduce(Int::times)

	override fun part2() =
		determineWinningTimes(singleRace)
}