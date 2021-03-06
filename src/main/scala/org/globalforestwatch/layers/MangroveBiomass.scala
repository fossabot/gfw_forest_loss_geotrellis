package org.globalforestwatch.layers

case class MangroveBiomass(grid: String) extends DoubleLayer with OptionalDLayer {
  val uri: String =
    s"$basePath/mangrove_biomass/$grid.tif"
}
