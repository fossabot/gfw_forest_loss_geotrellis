package org.globalforestwatch.summarystats.carbonflux

import org.apache.spark.sql.DataFrame
import org.globalforestwatch.summarystats.SummaryExport
import org.globalforestwatch.summarystats.carbonflux.dataframes._

object CarbonFluxExport extends SummaryExport {


  override protected def exportGadm(df: DataFrame,
                                    outputUrl: String,
                                    kwargs: Map[String, Any]): Unit = {

    def exportSummary(df: DataFrame): Unit = {

      val adm2ApiDF = df
        .transform(Adm2ApiDF.sumArea)
        .coalesce(40) // this should result in an avg file size of 100MB

      adm2ApiDF
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/adm2/summary")

      val adm1ApiDF = adm2ApiDF
        .transform(Adm1ApiDF.sumArea)
        .coalesce(12) // this should result in an avg file size of 100MB

      adm1ApiDF
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/adm1/summary")

      val isoApiDF = adm1ApiDF
        .transform(IsoApiDF.sumArea)
        .coalesce(4) // this should result in an avg file size of 100MB

      isoApiDF
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/iso/summary")

    }

    def exportChange(df: DataFrame): Unit = {
      val adm2ApiDF = df
        .transform(Adm2ApiDF.sumChange)
        .coalesce(100) // this should result in an avg file size of 100MB

      adm2ApiDF
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/adm2/change")

      val adm1ApiDF = adm2ApiDF
        .transform(Adm1ApiDF.sumChange)
        .coalesce(30) // this should result in an avg file size of 100MB

      adm1ApiDF
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/adm1/change")

      val isoApiDF = adm1ApiDF
        .transform(IsoApiDF.sumChange)
        .coalesce(10) // this should result in an avg file size of 100MB

      isoApiDF
        .write
        .options(csvOptions)
        .csv(path = outputUrl + "/iso/change")
    }

    val exportDF = df
      .transform(ApiDF.unpackValues)

    exportDF.cache()

    exportSummary(exportDF)
    exportChange(exportDF)

    exportDF.unpersist()

  }

}
