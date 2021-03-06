package org.globalforestwatch.annualupdate

import geotrellis.contrib.polygonal._
import geotrellis.contrib.vlm.geotiff._
import geotrellis.raster._
import geotrellis.vector.Extent
import org.scalatest._
/*
class TreeLossSummarySpec extends FunSpec with Matchers {

  val rs = GeoTiffRasterSource("s3://gfw2-data/forest_change/hansen_2018/50N_080W.tif")
  val rs2 = GeoTiffRasterSource("s3://gfw2-data/forest_cover/2000_treecover/Hansen_GFC2014_treecover2000_50N_080W.tif")
  val rs3 = GeoTiffRasterSource("s3://gfw2-data/climate/WHRC_biomass/WHRC_V4/Processed/50N_080W_t_aboveground_biomass_ha_2000.tif")

  val sampleExtent = Extent(-72.97754892, 43.85921846, -72.80676639, 43.97153490)

  val loss_raster: Raster[MultibandTile] = rs.read(sampleExtent).get
  val loss_tile: MultibandTile = loss_raster.tile.interpretAs(FloatUserDefinedNoDataCellType(0))
  val loss: Tile = loss_tile.band(0)

  val tcd_raster: Raster[MultibandTile] = rs2.read(Extent(-72.97754892, 43.85921846, -72.80676639, 43.97153490)).get
  val tcd_tile: MultibandTile = tcd_raster.tile
  val tcd: Tile = tcd_tile.band(0)

  val co2_raster: Raster[MultibandTile] = rs3.read(Extent(-72.97754892, 43.85921846, -72.80676639, 43.97153490)).get
  val co2_tile: MultibandTile = co2_raster.tile
  val co2: Tile = co2_tile.band(0)

  it("calculate summary") {
    // !!! This will throw an exception for failed requirement of matched cell types
    // val mbtile = MultibandTile(loss, tcd, co2)
    val composite_loss_raster: Raster[TreeLossTile] = Raster(TreeLossTile(loss, tcd, co2), loss_raster.extent)
    val summary = composite_loss_raster.polygonalSummary(sampleExtent, TreeLossSummary())
    summary.years.foreach { case (year, lossData: LossData) =>
      info(s"$year -> TCD: ${lossData.tcdHistogram.mean()} CO2: ${lossData.totalCo2}")
    }
    info(s"Summary: $summary")
  }
}
*/