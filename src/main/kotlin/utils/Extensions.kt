package utils
fun IntRange.intersects(other: IntRange) =
	other.first >= this.first && other.first <= this.last ||
		other.last >= this.first && other.last <= this.last ||
		this.first > other.first && this.last < other.last

fun LongRange.intersects(other: LongRange) =
	other.first >= this.first && other.first <= this.last ||
		other.last >= this.first && other.last <= this.last ||
		this.first > other.first && this.last < other.last
