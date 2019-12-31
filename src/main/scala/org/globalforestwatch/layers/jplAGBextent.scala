package org.globalforestwatch.layers

case class jplAGBextent(grid: String) extends BooleanLayer with OptionalILayer {
  val uri: String = s"$basePath/jpl_AGB_extent/$grid.tif"
}
