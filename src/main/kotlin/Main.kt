import kotlinx.datetime.Clock
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.measureTime

fun runPuzzle(day: Int): Boolean {
	val puzzleClass = Puzzle.forDay(day) ?: return false
	val constructor = puzzleClass.constructors.first()
	val input = Puzzle.getInput(day)
	val puzzle: Puzzle<Any>
	val part1: Any?
	val part2: Any?

	val setupTime = measureTime { puzzle = constructor.call(input) }
	val part1Time = measureTime { part1 = puzzle.part1() }
	val part2Time = measureTime { part2 = puzzle.part2() }

	println("""
		Day $day:
		  Setup time: $setupTime
		  Part 1: $part1 ($part1Time)
		  Part 2: $part2 ($part2Time)
	""".trimIndent())
	return true
}

fun runBenchmark(day: Int?) {
	println("Day  Part  Min            Average        Max")

	fun printStats(day: Int, part: Int, min: Duration, avg: Duration, max: Duration) {
		print(day.toString().padEnd(5))
		print(part.toString().padEnd(6))
		print(min.toString().padEnd(15))
		print(avg.toString().padEnd(15))
		print(max.toString().padEnd(15))
		println()
	}

	val runDays = if (day != null) day..day else 1..25

	for (i in runDays) {
		val p = Puzzle.forDay(i) ?: continue
		val instance = p.constructors.first().call(Puzzle.getInput(i))
		var min = Duration.INFINITE
		var max = Duration.ZERO
		var total = Duration.ZERO
		val iterations = 10000

		repeat(iterations) {
			val time = measureTime {
				instance.part1()
			}
			total += time
			if (time < min) min = time
			if (time > max) max = time
		}
		printStats(i, 1, min, total / iterations, max)

		min = Duration.INFINITE
		max = Duration.ZERO
		total = Duration.ZERO
		repeat(iterations) {
			val time = measureTime {
				instance.part2()
			}
			total += time
			if (time < min) min = time
			if (time > max) max = time
		}
		printStats(i, 2, min, total / iterations, max)
	}
}

fun main(args: Array<String>) {
	if (args.isNotEmpty()) {
		val puzzleToRun = args[0]
		val runDay: Int

		if (puzzleToRun == "today") {
			val zone = TimeZone.of("America/New_York")
			val time = Clock.System.now().toLocalDateTime(zone)
			val day = time.dayOfMonth

			if (day > 25 || time.month != Month.DECEMBER || time.year != 2023) {
				println("Advent of Code 2023 is over, so can't use today's date.")
				return
			}
			runDay = day
		} else if (puzzleToRun.toIntOrNull() != null) {
			runDay = puzzleToRun.toInt()
		} else if (puzzleToRun == "benchmark") {
			val day = args.getOrNull(1)?.toInt()
			runBenchmark(day)
			return
		} else {
			println("Invalid argument, must be a day from 1-25 or 'today' or 'benchmark'.")
			return
		}

		if (runDay !in 1..25) {
			println("Invalid day, must be between 1 and 25.")
			return
		}

		val ran = runPuzzle(runDay)
		if (!ran) {
			println("Day not implemented yet.")
		}

		return
	}

	for (day in 1..25) {
		val ran = runPuzzle(day)
		if (!ran) break
	}
}
