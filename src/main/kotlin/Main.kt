import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlin.time.Duration
import kotlin.time.measureTime

fun formatResult(result: Puzzle.Result) = buildString {
	appendLine("Day ${result.day}:")
	appendLine("  Setup time: ${result.setupTime}")
	append("  Part 1: ${result.part1 ?: "Unimplemented"} ")
	appendLine("(${result.part1Time})")
	append("  Part 2: ${result.part2 ?: "Unimplemented"} ")
	append("(${result.part2Time})")
}

fun runBenchmark() {
	for (i in 1..25) {
		val p = Puzzle.forDay(i) ?: break
		println("--- Day $i ---")
		val instance = p.constructors.first().call(Puzzle.getInput(i))
		var min = Duration.INFINITE
		var max = Duration.ZERO

		repeat(10000) {
			val time = measureTime {
				instance.part1()
			}
			if (time < min) min = time
			if (time > max) max = time
		}
		println("Part 1: min $min, max $max")
		min = Duration.INFINITE
		max = Duration.ZERO
		repeat(10000) {
			val time = measureTime {
				instance.part1()
			}
			if (time < min) min = time
			if (time > max) max = time
		}
		println("Part 2: min $min, max $max")
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
			runBenchmark()
			return
		} else {
			println("Invalid argument, must be a day from 1-25 or 'today' or 'benchmark'.")
			return
		}

		if (runDay !in 1..25) {
			println("Invalid day, must be between 1 and 25.")
			return
		}

		val result = Puzzle.runDay(runDay)
		if (result != null) {
			println(formatResult(result))
		} else {
			println("Day not implemented yet.")
		}

		return
	}

	for (day in 1..25) {
		val result = Puzzle.runDay(day) ?: break
		println(formatResult(result))
	}
}