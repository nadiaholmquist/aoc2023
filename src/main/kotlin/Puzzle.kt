import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.system.exitProcess

abstract class Puzzle(val day: Int) {
	abstract fun part1(): Int
	abstract fun part2(): Int

	val input: String
		get() {
			return testInput ?: puzzleInput
		}

	var testInput: String? = null
	val puzzleInput by lazy<String> {
		val inputsDir = System.getProperty("user.dir") + "/inputs"
		File(inputsDir).mkdir()
		val inputFileName = inputsDir + "/day${day}.txt"
		val inputFile = File(inputFileName)

		if (inputFile.exists()) {
			return@lazy inputFile.readText().trim()
		} else {
			try {
				val cookie = File("cookie.txt").readText().trim()
				val request = HttpRequest.newBuilder()
					.uri(URI("https://adventofcode.com/2023/day/${day}/input"))
					.header("Cookie", "session=${cookie}")
					.GET()
					.build()
				val client = HttpClient.newHttpClient()
				val response = client.send(request, HttpResponse.BodyHandlers.ofString())

				if (response.statusCode() != 200) {
					println("Got ${response.statusCode()} when fetching the puzzle input.\nMake sure the session cookie is correct. The response was:\n")
					println(response.body())
					exitProcess(1)
				}

				inputFile.createNewFile()

				val body = response.body()
				inputFile.writeText(body)
				return@lazy body.trim()
			} catch (e: Exception) {
				e.printStackTrace()
				print("Make sure you have cookie.txt with your Advent of Code session cookie.")
				exitProcess(1)
			}
		}
	}
}