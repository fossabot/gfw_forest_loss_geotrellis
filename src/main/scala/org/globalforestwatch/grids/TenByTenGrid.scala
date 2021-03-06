package org.globalforestwatch.grids

trait TenByTenGrid[T <: GridSources] extends Grid[T] {

  val pixelSize = 0.00025
  val gridSize = 10
  val rowCount = 10
  val blockSize = 400
}
