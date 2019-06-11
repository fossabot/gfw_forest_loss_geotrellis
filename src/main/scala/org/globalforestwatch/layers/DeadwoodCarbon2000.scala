package org.globalforestwatch.layers

case class DeadwoodCarbon2000(grid: String)
    extends DoubleLayer
      with OptionalDLayer {
  val uri: String = s"$basePath/deadwood_carbon_2000/$grid.tif"
}