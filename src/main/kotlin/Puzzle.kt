import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.reflect.KClass
import kotlin.reflect.safeCast
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.measureTime

abstract class Puzzle(val input: String) {
	open fun part1(): Int {
		throw NotImplementedError("Not implemented yet.")
	}

	open fun part2(): Int {
		throw NotImplementedError("Not implemented yet.")
	}

	data class Result(
		val day: Int,
		val part1: Int?,
		val part2: Int?,
		val setupTime: Duration,
		val part1Time: Duration,
		val part2Time: Duration
	)

	companion object {
		fun forDay(day: Int): KClass<Puzzle>? {
			return try {
				val kClass = Class.forName("puzzles.Day$day").kotlin
				kClass as KClass<Puzzle>
			} catch(e: Exception) {
				null
			}
		}

		fun runDay(day: Int, input: String? = null): Result? {
			val puzzleClass = forDay(day) ?: return null
			val instance: Puzzle
			var part1: Int? = null
			var part2: Int? = null
			var part1Time = Duration.ZERO
			var part2Time = Duration.ZERO

			val constructor = puzzleClass.constructors.first()

			val setupTime = measureTime {
				instance = constructor.call(input ?: getInput(day))
			}

			try {
				part1Time = measureTime { part1 = instance.part1() }
				part2Time = measureTime { part2 = instance.part2() }
			} catch (_: NotImplementedError) {}

			return Result(day, part1, part2, setupTime, part1Time, part2Time)
		}

		fun getInput(day: Int): String {
			val inputsDir = System.getProperty("user.dir") + "/inputs"
			File(inputsDir).mkdir()
			val inputFileName = inputsDir + "/day${day}.txt"
			val inputFile = File(inputFileName)

			if (inputFile.exists()) {
				return inputFile.readText().trim()
			}

			val cookie = File("cookie.txt").readText().trim()
			val request = HttpRequest.newBuilder()
				.uri(URI("https://adventofcode.com/2023/day/${day}/input"))
				.header("Cookie", "session=${cookie}")
				.GET()
				.build()
			val client = HttpClient.newHttpClient()
			val response = client.send(request, HttpResponse.BodyHandlers.ofString())

			if (response.statusCode() != 200) {
				throw Exception("Got status code ${response.statusCode()} when trying to fetch input.")
			}

			inputFile.createNewFile()

			val body = response.body()
			inputFile.writeText(body)
			return body.trim()
		}
	}
}