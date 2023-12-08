import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.reflect.KClass

abstract class Puzzle<out T>(val input: String) {
	open fun part1(): T {
		throw NotImplementedError("Not implemented yet.")
	}

	open fun part2(): T {
		throw NotImplementedError("Not implemented yet.")
	}

	companion object {
		fun forDay(day: Int): KClass<Puzzle<Any>>? {
			return try {
				val kClass = Class.forName("puzzles.Day$day").kotlin
				kClass as KClass<Puzzle<Any>>
			} catch (e: Exception) {
				null
			}
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

