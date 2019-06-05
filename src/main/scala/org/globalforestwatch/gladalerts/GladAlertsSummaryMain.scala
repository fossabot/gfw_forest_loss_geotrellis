package org.globalforestwatch.gladalerts

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import cats.implicits._
import com.monovore.decline.{CommandApp, Opts}
import geotrellis.vector.io.wkb.WKB
import geotrellis.vector.{Feature, Geometry}
import org.apache.log4j.Logger
import org.apache.spark._
import org.apache.spark.rdd._
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.globalforestwatch.features.GADMFeatureId
import org.locationtech.jts.precision.GeometryPrecisionReducer

object GladAlertsSummaryMain
    extends CommandApp(
      name = "geotrellis-tree-cover-loss",
      header = "Compute statistics on tree cover loss",
      main = {

        val featuresOpt =
          Opts.options[String]("features", "URI of features in TSV format")

        val outputOpt =
          Opts.option[String]("output", "URI of output dir for CSV files")

        // Can be used to increase the level of job parallelism
        val intputPartitionsOpt = Opts
          .option[Int]("input-partitions", "Partition multiplier for input")
          .withDefault(16)

        // Can be used to consolidate output into fewer files
        val outputPartitionsOpt = Opts
          .option[Int](
            "output-partitions",
            "Number of output partitions / files to be written"
          )
          .orNone

        val limitOpt = Opts
          .option[Int]("limit", help = "Limit number of records processed")
          .orNone

        val isoFirstOpt =
          Opts
            .option[String](
              "iso_first",
              help = "Filter by first letter of ISO code"
            )
            .orNone

        val isoStartOpt =
          Opts
            .option[String](
              "iso_start",
              help = "Filter by ISO code larger than or equal to given value"
            )
            .orNone

        val isoEndOpt =
          Opts
            .option[String](
              "iso_end",
              help = "Filter by ISO code smaller than given value"
            )
            .orNone

        val isoOpt =
          Opts.option[String]("iso", help = "Filter by country ISO code").orNone

        val admin1Opt = Opts
          .option[String]("admin1", help = "Filter by country Admin1 code")
          .orNone

        val admin2Opt = Opts
          .option[String]("admin2", help = "Filter by country Admin2 code")
          .orNone

        val logger = Logger.getLogger("TreeLossSummaryMain")

        (
          featuresOpt,
          outputOpt,
          intputPartitionsOpt,
          outputPartitionsOpt,
          limitOpt,
          isoOpt,
          isoFirstOpt,
          isoStartOpt,
          isoEndOpt,
          admin1Opt,
          admin2Opt
        ).mapN {
          (featureUris,
           outputUrl,
           inputPartitionMultiplier,
           maybeOutputPartitions,
           limit,
           iso,
           isoFirst,
           isoStart,
           isoEnd,
           admin1,
           admin2) =>
            val spark: SparkSession = GladAlertsSparkSession()
            import spark.implicits._

            // ref: https://github.com/databricks/spark-csv
            var featuresDF: DataFrame = spark.read
              .options(Map("header" -> "true", "delimiter" -> "\t"))
              .csv(featureUris.toList: _*)

            isoFirst.foreach { firstLetter =>
              featuresDF =
                featuresDF.filter(substring($"iso", 0, 1) === firstLetter(0))
            }

            isoStart.foreach { startCode =>
              featuresDF = featuresDF.filter($"iso" >= startCode)
            }

            isoEnd.foreach { endCode =>
              featuresDF = featuresDF.filter($"iso" < endCode)
            }

            iso.foreach { isoCode =>
              featuresDF = featuresDF.filter($"iso" === isoCode)
            }

            admin1.foreach { admin1Code =>
              featuresDF = featuresDF.filter($"gid_1" === admin1Code)
            }

            admin2.foreach { admin2Code =>
              featuresDF = featuresDF.filter($"gid_2" === admin2Code)
            }

            limit.foreach { n =>
              featuresDF = featuresDF.limit(n)
            }

            /* Transition from DataFrame to RDD in order to work with GeoTrellis features */
            val featureRDD: RDD[Feature[Geometry, GADMFeatureId]] =
              featuresDF.rdd.mapPartitions({
                iter: Iterator[Row] =>
                  for (i <- iter) yield {

                    // We need to reduce geometry precision  a bit to avoid issues like reported here
                    // https://github.com/locationtech/geotrellis/issues/2951
                    //
                    // Precision is set in src/main/resources/application.conf
                    // Here we use a fixed precision type and scale 1e11
                    // This is more than enough given that we work with 30 meter pixels
                    // and geometries already simplified to 1e4

                    val gpr = new GeometryPrecisionReducer(
                      geotrellis.vector.GeomFactory.precisionModel
                    )

                    def reduce(
                                gpr: org.locationtech.jts.precision.GeometryPrecisionReducer
                              )(g: geotrellis.vector.Geometry): geotrellis.vector.Geometry =
                      geotrellis.vector.Geometry(gpr.reduce(g.jtsGeom))

                    val countryCode: String = i.getString(1)
                    val admin1: String = i.getString(2)
                    val admin2: String = i.getString(3)
                    val geom: Geometry = reduce(gpr)(WKB.read(i.getString(4)))
                    Feature(geom, GADMFeatureId(countryCode, admin1, admin2))
                  }
              }, preservesPartitioning = true)

            val part = new HashPartitioner(
              partitions = featureRDD.getNumPartitions * inputPartitionMultiplier
            )

            val summaryRDD: RDD[(GADMFeatureId, GladAlertsSummary)] =
              GladAlertsRDD(featureRDD, GladAlertsGrid.blockTileGrid, part)

            val summaryDF =
              summaryRDD
                .flatMap {
                  case (id, gladAlertsSummary) =>
                    gladAlertsSummary.stats.map {
                      case (gladAlertsDataGroup, gladAlertsData) => {

                        val admin1: Integer = try {
                          id.admin1.split("[.]")(1).split("[_]")(0).toInt
                        } catch {
                          case e: Exception => null
                        }

                        val admin2: Integer = try {
                          id.admin2.split("[.]")(2).split("[_]")(0).toInt
                        } catch {
                          case e: Exception => null
                        }

                        GladAlertsRow(
                          id.country,
                          admin1,
                          admin2,
                          gladAlertsDataGroup.alertDate
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                          gladAlertsDataGroup.isConfirmed,
                          gladAlertsDataGroup.tile.x,
                          gladAlertsDataGroup.tile.y,
                          gladAlertsDataGroup.tile.z,
                          GladAlertsRowLayers(
                            gladAlertsDataGroup.climateMask,
                            gladAlertsDataGroup.primaryForest,
                            gladAlertsDataGroup.protectedAreas,
                            gladAlertsDataGroup.aze,
                            gladAlertsDataGroup.keyBiodiversityAreas,
                            gladAlertsDataGroup.landmark,
                            gladAlertsDataGroup.plantations,
                            gladAlertsDataGroup.mining,
                            gladAlertsDataGroup.logging,
                            gladAlertsDataGroup.rspo,
                            gladAlertsDataGroup.woodFiber,
                            gladAlertsDataGroup.peatlands,
                            gladAlertsDataGroup.indonesiaForestMoratorium,
                            gladAlertsDataGroup.oilPalm,
                            gladAlertsDataGroup.indonesiaForestArea,
                            gladAlertsDataGroup.peruForestConcessions,
                            gladAlertsDataGroup.oilGas,
                            gladAlertsDataGroup.mangroves2016,
                            gladAlertsDataGroup.intactForestLandscapes2016,
                            gladAlertsDataGroup.braBiomes
                          ),
                          gladAlertsData.totalAlerts,
                          gladAlertsData.totalArea,
                          gladAlertsData.totalCo2
                        )
                      }
                    }
                }
                .toDF(
                  "iso",
                  "adm1",
                  "adm2",
                  "alert_date",
                  "is_confirmed",
                  "x",
                  "y",
                  "z",
                  "layers",
                  "alert_count",
                  "area_ha",
                  "co2_emissions_Mt"
                )

            summaryDF.cache()

            val csvOptions = Map(
              "header" -> "true",
              "delimiter" -> "\t",
              "quote" -> "\u0000",
              "quoteMode" -> "NONE",
              "nullValue" -> "\u0000"
            )

            val runOutputUrl = outputUrl +
              "/gladAlerts_" + DateTimeFormatter
              .ofPattern("yyyyMMdd_HHmm")
              .format(LocalDateTime.now)

            val outputPartitionCount =
              maybeOutputPartitions.getOrElse(featureRDD.getNumPartitions)

            val tileDF = summaryDF
              .transform(TileDF.sumAlerts)

            tileDF
              .coalesce(1)
              .write
              .options(csvOptions)
              .csv(path = runOutputUrl + "/tiles")

            val adm2DailyDF = summaryDF
              .transform(Adm2DailyDF.unpackValues)
              .transform(Adm2DailyDF.sumAlerts)

            summaryDF.unpersist()

            adm2DailyDF
              .coalesce(1)
              .write
              .options(csvOptions)
              .csv(path = runOutputUrl + "/adm2_daily")

            val adm2WeeklyDF = adm2DailyDF.transform(Adm2WeeklyDF.sumAlerts)

            adm2WeeklyDF
              .coalesce(1)
              .write
              .options(csvOptions)
              .csv(path = runOutputUrl + "/adm2_weekly")

            val adm1WeeklyDF = adm2WeeklyDF
              .transform(Adm1WeeklyDF.sumAlerts)

            adm1WeeklyDF
              .coalesce(1)
              .write
              .options(csvOptions)
              .csv(path = runOutputUrl + "/adm1_weekly")

            val isoWeeklyDF = adm1WeeklyDF
              .transform(IsoWeeklyDF.sumAlerts)

            isoWeeklyDF
              .coalesce(1)
              .write
              .options(csvOptions)
              .csv(path = runOutputUrl + "/iso_weekly")

            spark.stop
        }
      }
    )
