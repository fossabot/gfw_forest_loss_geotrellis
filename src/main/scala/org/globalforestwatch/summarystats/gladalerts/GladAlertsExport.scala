package org.globalforestwatch.summarystats.gladalerts

import org.apache.spark.sql.DataFrame
import org.globalforestwatch.summarystats.SummaryExport
import org.globalforestwatch.summarystats.gladalerts.dataframes._
import org.globalforestwatch.util.Util.getAnyMapValue

object GladAlertsExport extends SummaryExport {

  override protected def exportGadm(summaryDF: DataFrame,
                                    outputUrl: String,
                                    kwargs: Map[String, Any]): Unit = {

    val changeOnly: Boolean =
      getAnyMapValue[Boolean](kwargs, "changeOnly")

    val buildDataCube: Boolean =
      getAnyMapValue[Boolean](kwargs, "buildDataCube")

    val minZoom: Int = {
      if (buildDataCube) 0 else 12
    } // TODO: remove magic numbers. maxZoom=12 should be in kwargs

    def exportTiles(tileDF: DataFrame): Unit = {

      if (buildDataCube) {
        tileDF
          .transform(TileDF.unpackValues)
          .transform(TileDF.sumAlerts)
          .write
          .options(csvOptions)
          .csv(path = outputUrl + "/tiles")
      }
    }

    def exportArea(df: DataFrame): Unit = {
      if (!changeOnly) {
        val adm2DF = df
          .transform(Adm2DailyDF.sumArea)

        adm2DF
          .coalesce(1)
          .write
          .options(csvOptions)
          .csv(path = outputUrl + "/adm2/summary")

        val adm1DF = adm2DF
          .transform(Adm1WeeklyDF.sumArea)

        adm1DF
          .coalesce(1)
          .write
          .options(csvOptions)
          .csv(path = outputUrl + "/adm1/summary")

        val isoDF = adm1DF
          .transform(IsoWeeklyDF.sumArea)

        isoDF
          .coalesce(1)
          .write
          .options(csvOptions)
          .csv(path = outputUrl + "/iso/summary")
      }
    }

    def exportAlerts(df: DataFrame): Unit = {

      val adm2DailyDF = df
        .transform(Adm2DailyDF.sumAlerts)

      adm2DailyDF
        .coalesce(1)
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/adm2/daily_alerts")

      val adm2DF = adm2DailyDF
        .transform(Adm2WeeklyDF.sumAlerts)

      adm2DF
        .coalesce(1)
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/adm2/weekly_alerts")

      val adm1DF = adm2DF
        .transform(Adm1WeeklyDF.sumAlerts)

      adm1DF
        .coalesce(1)
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/adm1/weekly_alerts")

      val isoDF = adm1DF
        .transform(IsoWeeklyDF.sumAlerts)

      isoDF
        .coalesce(1)
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/iso/weekly_alerts")
    }

    summaryDF.cache()
    exportTiles(summaryDF)
    val gadmDF = summaryDF.transform(Adm2DailyDF.unpackValues(minZoom))
    summaryDF.unpersist()

    gadmDF.cache()
    exportArea(gadmDF)
    exportAlerts(gadmDF)
    gadmDF.unpersist()
  }

  override protected def exportWdpa(summaryDF: DataFrame,
                                    outputUrl: String,
                                    kwargs: Map[String, Any]): Unit = {

    val changeOnly: Boolean =
      getAnyMapValue[Boolean](kwargs, "changeOnly")

    val buildDataCube: Boolean =
      getAnyMapValue[Boolean](kwargs, "buildDataCube")

    val minZoom: Int = {
      if (buildDataCube) 0 else 12
    } // TODO: remove magic numbers. maxZoom=12 should be in kwargs

    val wdpaDF = summaryDF
      .transform(WdpaDailyDF.unpackValues(minZoom))

    wdpaDF.cache()

    if (!changeOnly) {
      wdpaDF
        .transform(WdpaDailyDF.sumArea)
        .coalesce(1)
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/wdpa/summary")
    }

    wdpaDF
      .transform(WdpaDailyDF.sumAlerts)
      .coalesce(1)
      .write
      .options(csvOptions)
      .csv(path = outputUrl + "/wdpa/daily_alerts")

    wdpaDF
      .transform(WdpaWeeklyDF.sumAlerts)
      .coalesce(1)
      .write
      .options(csvOptions)
      .csv(path = outputUrl + "/wdpa/weekly_alerts")

    wdpaDF.unpersist()
  }

  override protected def exportFeature(summaryDF: DataFrame,
                                       outputUrl: String,
                                       kwargs: Map[String, Any]): Unit = {

    val changeOnly: Boolean =
      getAnyMapValue[Boolean](kwargs, "changeOnly")

    val buildDataCube: Boolean =
      getAnyMapValue[Boolean](kwargs, "buildDataCube")

    val minZoom: Int = {
      if (buildDataCube) 0 else 12
    } // TODO: remove magic numbers. maxZoom=12 should be in kwargs

    val featureDF = summaryDF
      .transform(SimpleFeatureDailyDF.unpackValues(minZoom))

    featureDF.cache()

    if (!changeOnly) {
      featureDF
        .transform(SimpleFeatureDailyDF.sumArea)
        .coalesce(1)
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/feature/summary")
    }

    featureDF
      .transform(SimpleFeatureDailyDF.sumAlerts)
      .coalesce(1)
      .write
      .options(csvOptions)
      .csv(path = outputUrl + "/feature/daily_alerts")

    featureDF
      .transform(SimpleFeatureWeeklyDF.sumAlerts)
      .coalesce(1)
      .write
      .options(csvOptions)
      .csv(path = outputUrl + "/feature/weekly_alerts")

    featureDF.unpersist()
  }

  override protected def exportGeostore(summaryDF: DataFrame,
                                        outputUrl: String,
                                        kwargs: Map[String, Any]): Unit = {

    val changeOnly: Boolean =
      getAnyMapValue[Boolean](kwargs, "changeOnly")

    val buildDataCube: Boolean =
      getAnyMapValue[Boolean](kwargs, "buildDataCube")

    val minZoom: Int = {
      if (buildDataCube) 0 else 12
    } // TODO: remove magic numbers. maxZoom=12 should be in kwargs

    val featureDF = summaryDF
      .transform(GeostoreFeatureDailyDF.unpackValues(minZoom))

    featureDF.cache()

    if (!changeOnly) {
      featureDF
        .transform(GeostoreFeatureDailyDF.sumArea)
        .coalesce(1)
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/geostore/summary")
    }

    featureDF
      .transform(GeostoreFeatureDailyDF.sumAlerts)
      .coalesce(1)
      .write
      .options(csvOptions)
      .csv(path = outputUrl + "/geostore/daily_alerts")

    featureDF
      .transform(GeostoreFeatureWeeklyDF.sumAlerts)
      .coalesce(1)
      .write
      .options(csvOptions)
      .csv(path = outputUrl + "/geostore/weekly_alerts")

    featureDF.unpersist()
  }
}
