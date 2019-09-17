package org.globalforestwatch.summarystats.treecoverloss

import cats.implicits._
import geotrellis.contrib.polygonal.CellVisitor
import geotrellis.raster._
import org.globalforestwatch.summarystats.Summary
import org.globalforestwatch.util.Geodesy
import org.globalforestwatch.util.Util.getAnyMapValue

/** LossData Summary by year */
case class TreeLossSummary(stats: Map[TreeLossDataGroup, TreeLossData] =
                           Map.empty,
                           kwargs: Map[String, Any])
  extends Summary[TreeLossSummary] {

  /** Combine two Maps and combine their LossData when a year is present in both */
  def merge(other: TreeLossSummary): TreeLossSummary = {
    // the years.combine method uses LossData.lossDataSemigroup instance to perform per value combine on the map
    TreeLossSummary(stats.combine(other.stats), kwargs)
  }
}

object TreeLossSummary {
  // TreeLossSummary form Raster[TreeLossTile] -- cell types may not be the same

  implicit val mdhCellRegisterForTreeLossRaster1
  : CellVisitor[Raster[TreeLossTile], TreeLossSummary] =
    new CellVisitor[Raster[TreeLossTile], TreeLossSummary] {

      def register(raster: Raster[TreeLossTile],
                   col: Int,
                   row: Int,
                   acc: TreeLossSummary): TreeLossSummary = {

        val tcdYear: Int = getAnyMapValue[Int](acc.kwargs, "tcdYear")

        // This is a pixel by pixel operation
        val loss: Integer = raster.tile.loss.getData(col, row)
        val gain: Integer = raster.tile.gain.getData(col, row)
        val tcd2000: Integer = raster.tile.tcd2000.getData(col, row)
        val tcd2010: Integer = raster.tile.tcd2010.getData(col, row)
        val biomass: Double = raster.tile.biomass.getData(col, row)
        val primaryForest: Boolean = raster.tile.primaryForest.getData(col, row)

        val cols: Int = raster.rasterExtent.cols
        val rows: Int = raster.rasterExtent.rows
        val ext = raster.rasterExtent.extent
        val cellSize = raster.cellSize

        val lat: Double = raster.rasterExtent.gridRowToMap(row)
        val area: Double = Geodesy.pixelArea(lat, raster.cellSize) // uses Pixel's center coordiate.  +- raster.cellSize.height/2 doesn't make much of a difference

        val areaHa = area / 10000.0

        val gainArea: Double = gain * areaHa

        val co2Factor = 0.5 * 44 / 12

        val biomassPixel = biomass * areaHa
        val co2Pixel = biomassPixel * co2Factor

        val thresholds = (0 until 100 by 5).toList

        def updateSummary(
                           thresholds: List[Int],
                           stats: Map[TreeLossDataGroup, TreeLossData]
                         ): Map[TreeLossDataGroup, TreeLossData] = {
          if (thresholds == Nil) stats
          else {
            val pKey =
              TreeLossDataGroup(thresholds.head, tcdYear, primaryForest)

            val summary: TreeLossData =
              stats.getOrElse(
                key = pKey,
                default =
                  TreeLossData(TreeLossYearDataMap.empty, 0, 0, 0, 0, 0, 0, 0)
              )

            summary.totalArea += areaHa
            summary.totalGainArea += gainArea

            if ((tcd2000 >= thresholds.head && tcdYear == 2000) || (tcd2010 >= thresholds.head && tcdYear == 2010)) {

              if (loss != null) {
                summary.lossYear(loss).area_loss += areaHa
                summary.lossYear(loss).biomass_loss += biomassPixel
                summary.lossYear(loss).carbon_emissions += co2Pixel
              }

              // TODO: use extent2010 to calculate avg biomass incase year is selected
              summary.avgBiomass = ((summary.avgBiomass * summary.extent2000) + (biomass * areaHa)) / (summary.extent2000 + areaHa)
              if (tcdYear == 2000) summary.extent2000 += areaHa
              else if (tcdYear == 2010) summary.extent2010 += areaHa
              summary.totalBiomass += biomassPixel
              summary.totalCo2 += co2Pixel
            }

            if (tcd2010 >= thresholds.head && tcdYear == 2000)
              summary.extent2010 += areaHa
            else if (tcd2000 >= thresholds.head && tcdYear == 2010)
              summary.extent2000 += areaHa

            updateSummary(thresholds.tail, stats.updated(pKey, summary))
          }
        }

        val updatedSummary: Map[TreeLossDataGroup, TreeLossData] =
          updateSummary(thresholds, acc.stats)

        TreeLossSummary(updatedSummary, acc.kwargs)

      }
    }
}