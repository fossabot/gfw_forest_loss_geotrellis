package org.globalforestwatch.layers

case class IndonesiaForestMoratorium(grid: String)
    extends BooleanLayer
    with OptionalILayer {
  val uri: String =
    s"$basePath/idn_forest_moratorium/$grid.tif"
}
