package puzzles

import Puzzle
import java.lang.Exception

class Day10(input: String) : Puzzle<Int>(input) {
	enum class Direction {
		NORTH, EAST, SOUTH, WEST;

		fun offset(): Pair<Int, Int> {
			return when (this) {
				NORTH -> 0 to -1
				SOUTH -> 0 to 1
				WEST -> -1 to 0
				EAST -> 1 to 0
			}
		}

		fun opposite(): Direction {
			return when (this) { NORTH -> SOUTH; SOUTH -> NORTH; WEST -> EAST; EAST -> WEST }
		}
	}
	enum class Tile {
		NONE, HORIZONTAL, VERTICAL,
		NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST;

		companion object {
			fun fromChar(char: Char): Tile {
				val chars = " -|LJF7"
				if (char !in chars) return NONE
				return Tile.entries[chars.indexOf(char)]
			}
		}

		override fun toString(): String {
			val pipeChars = ".─│└┘┌┐"
			return pipeChars[this.ordinal].toString()
		}

		fun connectsDirections(): Set<Direction> {
			return when (this) {
				HORIZONTAL -> setOf(Direction.WEST, Direction.EAST)
				VERTICAL -> setOf(Direction.NORTH, Direction.SOUTH)
				NORTH_EAST -> setOf(Direction.NORTH, Direction.EAST)
				NORTH_WEST -> setOf(Direction.NORTH, Direction.WEST)
				SOUTH_EAST -> setOf(Direction.SOUTH, Direction.EAST)
				SOUTH_WEST -> setOf(Direction.SOUTH, Direction.WEST)
				NONE -> emptySet()
			}
		}
	}

	fun parseMap(): Pair<List<List<Tile>>, Pair<Int, Int>> {
		var start: Pair<Int, Int>? = null
		val tiles = input.lines().mapIndexed { i, line ->
			line.mapIndexed { j, char ->
				var useChar = char
				if (char == 'S') {
					start = j to i
					useChar = ' '
				}
				Tile.fromChar(useChar)
			}.toMutableList()
		}
		if (start == null) throw Exception("Did not find start")

		val (x, y) = start!!
		val startConnections = Direction.entries.filter {
			when (it) {
				Direction.NORTH -> tiles[y-1][x] in setOf(Tile.VERTICAL, Tile.SOUTH_WEST, Tile.SOUTH_EAST)
				Direction.SOUTH -> tiles[y+1][x] in setOf(Tile.VERTICAL, Tile.NORTH_WEST, Tile.NORTH_EAST)
				Direction.WEST -> tiles[y][x-1] in setOf(Tile.HORIZONTAL, Tile.NORTH_EAST, Tile.SOUTH_EAST)
				Direction.EAST -> tiles[y][x+1] in setOf(Tile.HORIZONTAL, Tile.NORTH_WEST, Tile.SOUTH_WEST)
			}
		}
		val startDirection = Tile.entries.find { it.connectsDirections() == startConnections.toSet() }!!
		tiles[y][x] = startDirection

		return tiles to start!!
	}

	val tiles: List<List<Tile>>
	val start: Pair<Int, Int>

	init {
		val (tiles, start) = parseMap()
		this.tiles = tiles
		this.start = start
	}

	fun visualize(tiles: List<List<Char>>, highlight: List<Pair<Int, Int>>) {
		val map = tiles.mapIndexed { y, line ->
			line.mapIndexed { x, char ->
				if (x to y in highlight) Char(0x1B) + "[1;42m${char}" + Char(0x1B) + "[0m"
				else "$char"
			}.joinToString("")
		}.filter { it.isNotEmpty() }.joinToString("\n")
		println(map)
	}

	operator fun List<List<Tile>>.get(it: Pair<Int, Int>) = this[it.second][it.first]

	fun findPath(): List<Pair<Pair<Int, Int>, Tile>> {
		var currTile = start
		var lastDir = tiles[start].connectsDirections().first()
		var points = mutableListOf<Pair<Pair<Int, Int>, Tile>>()
		do {
			points.add(currTile to tiles[currTile])
			val dirs = tiles[currTile].connectsDirections()
			val dir = dirs.find { it != lastDir }!!
			val offset = dir.offset()
			currTile = currTile.first + offset.first to currTile.second + offset.second
			lastDir = dir.opposite()
		} while (currTile != start)

		return points
	}

	override fun part1() = findPath().count() / 2

	override fun part2(): Int {
		val path = findPath()

		val out = MutableList(tiles.size) { _ ->
			MutableList(tiles[0].size) { Tile.NONE }
		}

		path.forEach { (pos, tile) ->
			val (x, y) = pos
			out[y][x] = tile
		}

		var insidePath: Boolean
		var found = 0
		var lastDir: Tile
		out.forEach { row ->
			insidePath = false
			lastDir = Tile.NONE

			row.forEach { tile ->
				when (tile) {
					Tile.VERTICAL -> insidePath = !insidePath
					Tile.NORTH_EAST, Tile.SOUTH_EAST -> {
						lastDir = tile
						insidePath = !insidePath
					}
					Tile.NORTH_WEST, Tile.SOUTH_WEST -> {
						val dir = if (tile == Tile.NORTH_WEST) Tile.NORTH_EAST else Tile.SOUTH_EAST
						if (dir == lastDir) insidePath = !insidePath
					}
					Tile.NONE -> {
						if (insidePath) {
							found++
						}
					}
					else -> {}
				}
			}
		}

		return found
	}
}
