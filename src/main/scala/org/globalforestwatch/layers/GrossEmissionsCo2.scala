package org.globalforestwatch.layers

case class GrossEmissionsCo2(grid: String)
    extends DoubleLayer
      with OptionalDLayer {
  val uri: String = s"$basePath/gross_emissions_co2/$grid.tif"
}