package puzzles

import Puzzle

class Day2 : Puzzle(2) {
    enum class CubeColor {
        RED, GREEN, BLUE;

        companion object {
            fun fromString(str: String) = when (str) {
                "red" -> RED
                "green" -> GREEN
                "blue" -> BLUE
                else -> throw IllegalArgumentException("Invalid color $str")
            }
        }
    }

    data class Game(val number: Int, val rounds: List<Map<CubeColor, Int>>) {
        companion object {
            fun fromLine(str: String): Game {
                val parts = str.split(":")
                val num = parts[0].removePrefix("Game ")
                val rounds = parts[1].split(";")
                    .asSequence()
                    .map { it.trim() }
                    .map {
                        it.split(", ").associate {
                            val parts = it.split(" ")
                            CubeColor.fromString(parts[1]) to parts[0].toInt()
                        }
                    }.toList()

                return Game(num.toInt(), rounds)
            }
        }
    }

    val games = input.lines().map(Game::fromLine)
    val maxCubes = mapOf(
        CubeColor.RED to 12,
        CubeColor.GREEN to 13,
        CubeColor.BLUE to 14
    )

    override fun part1() =
        games.filter {
            it.rounds.all { round ->
                round.none { set ->
                    maxCubes[set.key]!! < set.value
                }
            }
        }.sumOf { it.number }

    override fun part2() =
        games.sumOf {
            it.rounds.fold(mutableMapOf<CubeColor, Int>()) { acc, round ->
                round.forEach { set ->
                    val current = acc.getOrDefault(set.key, 0)
                    if (current < set.value) acc[set.key] = set.value
                }
                acc
            }.values.reduce(Int::times)
        }
}