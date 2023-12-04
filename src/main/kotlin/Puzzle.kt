import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.reflect.KClass
import kotlin.reflect.safeCast
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

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
		val setupTime: Long,
		val part1Time: Long,
		val part2Time: Long
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
			var part1Time = 0L
			var part2Time = 0L

			val constructor = puzzleClass.constructors.first()

			val setupTime = measureTimeMillis {
				instance = constructor.call(input ?: getInput(day))
			}
			try {
				part1Time = measureTimeMillis { part1 = instance.part1() }
				part2Time = measureTimeMillis { part2 = instance.part2() }
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