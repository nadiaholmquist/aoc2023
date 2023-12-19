package puzzles

import Puzzle

class Day19(input: String) : Puzzle<Long>(input) {
	data class Item(val x: Int, val m: Int, val a: Int, val s: Int) {
		operator fun get(rating: Rating) = when (rating) {
			Rating.X -> x; Rating.M -> m; Rating.A -> a; Rating.S -> s
		}
		companion object {
			private val itemRegex = Regex("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}")
			fun fromString(str: String): Item {
				val (_, x, m, a, s) = itemRegex.matchEntire(str)?.groupValues ?: throw IllegalArgumentException()
				return Item(x.toInt(), m.toInt(), a.toInt(), s.toInt())
			}
		}
	}

	data class ItemFilter(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
		operator fun get(rating: Rating) = when (rating) {
			Rating.X -> x; Rating.M -> m; Rating.A -> a; Rating.S -> s
		}
		fun withNew(rating: Rating, filter: IntRange) = when (rating) {
			Rating.X -> this.copy(x = filter)
			Rating.M -> this.copy(m = filter)
			Rating.A -> this.copy(a = filter)
			Rating.S -> this.copy(s = filter)
		}
	}

	sealed interface Target {
		data object Reject : Target
		data object Accept : Target
		data class Next(val workflow: String) : Target
		companion object {
			fun fromString(str: String): Target = when (str) {
				"R" -> Reject
				"A" -> Accept
				else -> Next(str)
			}
		}
	}

	enum class Rating {
		X, M, A, S;
		companion object {
			val chars = mapOf('x' to X, 'm' to M, 'a' to A, 's' to S)
		}
	}

	sealed class Rule(val target: Target) {
		class Greater(val rating: Rating, val value: Int, target: Target) : Rule(target)
		class Less(val rating: Rating, val value: Int, target: Target) : Rule(target)
		class Always(target: Target) : Rule(target)

		fun evaluate(item: Item) = when (this) {
			is Always -> true
			is Greater -> item[rating] > value
			is Less -> item[rating] < value
		}

		fun evaluate(filter: ItemFilter): Pair<ItemFilter?, ItemFilter?> = when (this) {
			is Always -> filter to filter
			is Greater -> {
				val ratingFilter = filter[rating]
				when {
					value > ratingFilter.last -> filter to null
					value < ratingFilter.first -> null to filter
					else -> {
						val rf = ratingFilter.first..value
						val rt = value + 1..ratingFilter.last
						filter.withNew(rating, rf) to filter.withNew(rating, rt)
					}
				}
			}
			is Less -> {
				val ratingFilter = filter[rating]
				when {
					value > ratingFilter.last -> null to filter
					value < ratingFilter.first -> filter to null
					else -> {
						val rt = ratingFilter.first..<value
						val rf = value..ratingFilter.last
						filter.withNew(rating, rf) to filter.withNew(rating, rt)
					}
				}
			}
		}

		companion object {
			private val ruleRegex = Regex("([a-z]+|\\d+)([><])([a-z]+|\\d+):([a-z]+|[AR])")
			fun fromString(str: String): Rule {
				return if (':' !in str) {
					Always(Target.fromString(str))
				} else {
					val (_, rating, type, value, targetStr) = ruleRegex.matchEntire(str)?.groupValues ?: throw IllegalArgumentException()
					val target = Target.fromString(targetStr)
					if (type == ">") Greater(Rating.chars[rating.first()]!!, value.toInt(), target)
					else Less(Rating.chars[rating.first()]!!, value.toInt(), target)
				}
			}
		}

		override fun toString(): String {
			return when (this) {
				is Always -> "$target"
				is Greater -> "$rating > $value"
				is Less -> "$rating < $value"
			}
		}
	}

	fun parseRules(lines: List<String>): Map<String, List<Rule>> {
		val ruleRegex = Regex("([a-z]+)\\{(.+)}")
		return lines.associate { line ->
			val (_, name, ruleList) = ruleRegex.matchEntire(line)?.groupValues ?: throw IllegalArgumentException()
			val rules = ruleList.split(",").map(Rule::fromString)
			name to rules
		}
	}

	fun List<Rule>.evaluate(item: Item): Target {
		this.forEach {
			val result = it.evaluate(item)
			if (result) return it.target
		}
		throw IllegalStateException("Rule evaluation should never reach here.")
	}

	class Node<out T> private constructor(private val next0: Node<T>?, private val next1: Node<T>?, val value: T) {
		constructor(value: T, next0: Node<T>, next1: Node<T>) : this(next0, next1, value)
		constructor(value: T) : this(null, null, value)

		val hasNext = next0 != null

		operator fun get(n: Boolean): Node<T> {
			if (next0 == null) throw NoSuchElementException("This node has no child nodes")
			return if (n) next1!! else next0
		}
		operator fun component1() = this[false]
		operator fun component2() = this[true]
		override fun toString(): String {
			return buildString {
				append(value.toString())
				if (next0 != null) {
					appendLine(" {")
					appendLine(next0.toString().prependIndent("  "))
					appendLine(next1.toString().prependIndent("  "))
					append("}")
				} else {
				}
			}
		}
	}

	val lines = input.lines()
	val workflows = parseRules(lines.takeWhile { it.isNotBlank() })

	fun buildWorkflowTree(workflows: Map<String, List<Rule>>): Node<Rule> {
		val start = workflows["in"]!!

		fun buildFrom(rules: List<Rule>): Node<Rule> {
			val firstRule = rules.first()
			return if (firstRule is Rule.Always) {
				when (val target = firstRule.target) {
					is Target.Accept, Target.Reject -> Node(firstRule)
					is Target.Next -> buildFrom(workflows[target.workflow]!!)
				}
			} else {
				val trueNode = when (val target = firstRule.target) {
					is Target.Accept, Target.Reject -> Node(Rule.Always(target))
					is Target.Next -> buildFrom(workflows[target.workflow]!!)
				}
				val falseNode = buildFrom(rules.drop(1))
				Node(firstRule, falseNode, trueNode)
			}
		}

		return buildFrom(start)
	}

	override fun part1(): Long {
		val items = lines.drop(workflows.size + 1).map { Item.fromString(it) }
		val startWorkflow = workflows["in"]!!

		val accepted = items.filter { item ->
			var workflow = startWorkflow
			while (true) {
				when (val result = workflow.evaluate(item)) {
					is Target.Accept -> return@filter true
					is Target.Reject -> return@filter false
					is Target.Next -> workflow = workflows[result.workflow]!!
				}
			}
			return@filter false
		}

		return accepted.sumOf { it.x + it.m + it.a + it.s }.toLong()
	}

	override fun part2(): Long {
		val tree = buildWorkflowTree(workflows)
		val filter = ItemFilter(1..4000, 1..4000, 1..4000, 1..4000)

		fun walk(node: Node<Rule>, filter: ItemFilter): List<ItemFilter> {
			if (!node.hasNext) {
				return when (node.value.target) {
					is Target.Accept -> listOf(filter)
					is Target.Reject -> emptyList()
					else -> throw IllegalStateException()
				}
			} else {
				val (falseFilter, trueFilter) = node.value.evaluate(filter)
				val falseResult = falseFilter?.let { walk(node[false], falseFilter) } ?: emptyList()
				val trueResult = trueFilter?.let { walk(node[true], trueFilter) } ?: emptyList()
				return falseResult + trueResult
			}
		}

		fun IntRange.length() = this.last - this.first + 1

		return walk(tree, filter).sumOf {
			it.x.length().toLong() * it.m.length() * it.a.length() * it.s.length()
		}
	}
}