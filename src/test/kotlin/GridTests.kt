import utils.TileGrid
import utils.map
import utils.subGrid
import kotlin.test.Test
import kotlin.test.assertEquals

class GridTests {
	val simpleList = listOf(1,2,3,4,5,6,7,8,9,10,11,12)

	@Test fun `Creating it returns right dimensions`() {
		val grid = TileGrid(simpleList, 4)
		assertEquals(4, grid.width)
		assertEquals(3, grid.height)
	}

	@Test fun `Create with lambda`() {
		val grid = TileGrid(4, 4) { x, y ->
			x*y
		}
		println(grid)
	}

	@Test fun `Get returns right values`() {
		val grid = TileGrid(simpleList, 4)
		assertEquals(1, grid.get(0, 0))
		assertEquals(9, grid.get(0, 2))
		assertEquals(4, grid.get(3, 0))
		assertEquals(12, grid.get(3, 2))
	}

	@Test fun `Subgrids behave correctly`() {
		val grid = TileGrid(simpleList, 4)
		val sub = grid.subGrid(1..2, 1..2)
		assertEquals(2, sub.width)
		assertEquals(2, sub.height)
		sub.set(1, 0, 100)
		assertEquals(100, grid.get(2, 1))
	}

	@Test fun `Call toString on a Grid`() {
		val grid = TileGrid(simpleList, 4)
		println(grid.toString())
	}

	@Test fun `Transforming a grid`() {
		val grid = TileGrid(simpleList, 4)
		val newGrid = grid.map { it * 2 }
		assertEquals(24, newGrid.get(3, 2))
	}
}