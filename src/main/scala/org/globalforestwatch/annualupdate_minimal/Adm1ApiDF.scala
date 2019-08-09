package org.globalforestwatch.annualupdate_minimal

import org.apache.spark.sql._
import org.apache.spark.sql.functions._

object Adm1ApiDF {

  def sumArea(df: DataFrame): DataFrame = {

    val spark: SparkSession = df.sparkSession
    import spark.implicits._

    df.groupBy(
        $"iso",
        $"adm1",
        $"threshold",
        $"tcs",
        $"global_land_cover",
        $"primary_forest",
//        $"idn_primary_forest",
//        $"erosion",
//        $"biodiversity_significance",
//        $"biodiversity_intactness",
        $"wdpa",
        $"aze",
        $"plantations",
//        $"river_basin",
//        $"ecozone",
//        $"urban_watershed",
      $"mangroves_1996",
      $"mangroves_2016",
//        $"water_stress",
        $"ifl",
//        $"endemic_bird_area",
        $"tiger_cl",
        $"landmark",
        $"land_right",
        $"kba",
        $"mining",
//        $"rspo",
      $"idn_mys_peatlands",
        $"oil_palm",
        $"idn_forest_moratorium",
//        $"idn_land_cover",
//        $"mex_protected_areas",
//        $"mex_pes",
//        $"mex_forest_zoning",
//        $"per_production_forest",
//        $"per_protected_area",
//        $"per_forest_concession",
//        $"bra_biomes",
        $"wood_fiber",
        $"resource_right",
        $"managed_forests",
//        $"oil_gas",
        $"year_2001",
        $"year_2002",
        $"year_2003",
        $"year_2004",
        $"year_2005",
        $"year_2006",
        $"year_2007",
        $"year_2008",
        $"year_2009",
        $"year_2010",
        $"year_2011",
        $"year_2012",
        $"year_2013",
        $"year_2014",
        $"year_2015",
        $"year_2016",
        $"year_2017",
        $"year_2018"
      )
      .agg(
        sum("total_area") as "total_area",
        sum("extent_2000") as "extent_2000",
        sum("extent_2010") as "extent_2010",
        sum("total_gain") as "total_gain",
        sum("total_biomass") as "total_biomass",
        sum($"avg_biomass_per_ha" * $"extent_2000") as "weighted_biomass_per_ha",
        sum("total_co2") as "total_co2",
//        sum("total_mangrove_biomass") as "total_mangrove_biomass",
//        sum($"avg_mangrove_biomass_per_ha" * $"extent_2000") as "weighted_mangrove_biomass_per_ha",
//        sum("total_mangrove_co2") as "total_mangrove_co2",
        sum("area_loss_2001") as "area_loss_2001",
        sum("area_loss_2002") as "area_loss_2002",
        sum("area_loss_2003") as "area_loss_2003",
        sum("area_loss_2004") as "area_loss_2004",
        sum("area_loss_2005") as "area_loss_2005",
        sum("area_loss_2006") as "area_loss_2006",
        sum("area_loss_2007") as "area_loss_2007",
        sum("area_loss_2008") as "area_loss_2008",
        sum("area_loss_2009") as "area_loss_2009",
        sum("area_loss_2010") as "area_loss_2010",
        sum("area_loss_2011") as "area_loss_2011",
        sum("area_loss_2012") as "area_loss_2012",
        sum("area_loss_2013") as "area_loss_2013",
        sum("area_loss_2014") as "area_loss_2014",
        sum("area_loss_2015") as "area_loss_2015",
        sum("area_loss_2016") as "area_loss_2016",
        sum("area_loss_2017") as "area_loss_2017",
        sum("area_loss_2018") as "area_loss_2018",
        sum("biomass_loss_2001") as "biomass_loss_2001",
        sum("biomass_loss_2002") as "biomass_loss_2002",
        sum("biomass_loss_2003") as "biomass_loss_2003",
        sum("biomass_loss_2004") as "biomass_loss_2004",
        sum("biomass_loss_2005") as "biomass_loss_2005",
        sum("biomass_loss_2006") as "biomass_loss_2006",
        sum("biomass_loss_2007") as "biomass_loss_2007",
        sum("biomass_loss_2008") as "biomass_loss_2008",
        sum("biomass_loss_2009") as "biomass_loss_2009",
        sum("biomass_loss_2010") as "biomass_loss_2010",
        sum("biomass_loss_2011") as "biomass_loss_2011",
        sum("biomass_loss_2012") as "biomass_loss_2012",
        sum("biomass_loss_2013") as "biomass_loss_2013",
        sum("biomass_loss_2014") as "biomass_loss_2014",
        sum("biomass_loss_2015") as "biomass_loss_2015",
        sum("biomass_loss_2016") as "biomass_loss_2016",
        sum("biomass_loss_2017") as "biomass_loss_2017",
        sum("biomass_loss_2018") as "biomass_loss_2018",
        sum("carbon_emissions_2001") as "carbon_emissions_2001",
        sum("carbon_emissions_2002") as "carbon_emissions_2002",
        sum("carbon_emissions_2003") as "carbon_emissions_2003",
        sum("carbon_emissions_2004") as "carbon_emissions_2004",
        sum("carbon_emissions_2005") as "carbon_emissions_2005",
        sum("carbon_emissions_2006") as "carbon_emissions_2006",
        sum("carbon_emissions_2007") as "carbon_emissions_2007",
        sum("carbon_emissions_2008") as "carbon_emissions_2008",
        sum("carbon_emissions_2009") as "carbon_emissions_2009",
        sum("carbon_emissions_2010") as "carbon_emissions_2010",
        sum("carbon_emissions_2011") as "carbon_emissions_2011",
        sum("carbon_emissions_2012") as "carbon_emissions_2012",
        sum("carbon_emissions_2013") as "carbon_emissions_2013",
        sum("carbon_emissions_2014") as "carbon_emissions_2014",
        sum("carbon_emissions_2015") as "carbon_emissions_2015",
        sum("carbon_emissions_2016") as "carbon_emissions_2016",
        sum("carbon_emissions_2017") as "carbon_emissions_2017",
        sum("carbon_emissions_2018") as "carbon_emissions_2018"
//        sum("mangrove_biomass_loss_2001") as "mangrove_biomass_loss_2001",
//        sum("mangrove_biomass_loss_2002") as "mangrove_biomass_loss_2002",
//        sum("mangrove_biomass_loss_2003") as "mangrove_biomass_loss_2003",
//        sum("mangrove_biomass_loss_2004") as "mangrove_biomass_loss_2004",
//        sum("mangrove_biomass_loss_2005") as "mangrove_biomass_loss_2005",
//        sum("mangrove_biomass_loss_2006") as "mangrove_biomass_loss_2006",
//        sum("mangrove_biomass_loss_2007") as "mangrove_biomass_loss_2007",
//        sum("mangrove_biomass_loss_2008") as "mangrove_biomass_loss_2008",
//        sum("mangrove_biomass_loss_2009") as "mangrove_biomass_loss_2009",
//        sum("mangrove_biomass_loss_2010") as "mangrove_biomass_loss_2010",
//        sum("mangrove_biomass_loss_2011") as "mangrove_biomass_loss_2011",
//        sum("mangrove_biomass_loss_2012") as "mangrove_biomass_loss_2012",
//        sum("mangrove_biomass_loss_2013") as "mangrove_biomass_loss_2013",
//        sum("mangrove_biomass_loss_2014") as "mangrove_biomass_loss_2014",
//        sum("mangrove_biomass_loss_2015") as "mangrove_biomass_loss_2015",
//        sum("mangrove_biomass_loss_2016") as "mangrove_biomass_loss_2016",
//        sum("mangrove_biomass_loss_2017") as "mangrove_biomass_loss_2017",
//        sum("mangrove_biomass_loss_2018") as "mangrove_biomass_loss_2018",
//        sum("mangrove_carbon_emissions_2001") as "mangrove_carbon_emissions_2001",
//        sum("mangrove_carbon_emissions_2002") as "mangrove_carbon_emissions_2002",
//        sum("mangrove_carbon_emissions_2003") as "mangrove_carbon_emissions_2003",
//        sum("mangrove_carbon_emissions_2004") as "mangrove_carbon_emissions_2004",
//        sum("mangrove_carbon_emissions_2005") as "mangrove_carbon_emissions_2005",
//        sum("mangrove_carbon_emissions_2006") as "mangrove_carbon_emissions_2006",
//        sum("mangrove_carbon_emissions_2007") as "mangrove_carbon_emissions_2007",
//        sum("mangrove_carbon_emissions_2008") as "mangrove_carbon_emissions_2008",
//        sum("mangrove_carbon_emissions_2009") as "mangrove_carbon_emissions_2009",
//        sum("mangrove_carbon_emissions_2010") as "mangrove_carbon_emissions_2010",
//        sum("mangrove_carbon_emissions_2011") as "mangrove_carbon_emissions_2011",
//        sum("mangrove_carbon_emissions_2012") as "mangrove_carbon_emissions_2012",
//        sum("mangrove_carbon_emissions_2013") as "mangrove_carbon_emissions_2013",
//        sum("mangrove_carbon_emissions_2014") as "mangrove_carbon_emissions_2014",
//        sum("mangrove_carbon_emissions_2015") as "mangrove_carbon_emissions_2015",
//        sum("mangrove_carbon_emissions_2016") as "mangrove_carbon_emissions_2016",
//        sum("mangrove_carbon_emissions_2017") as "mangrove_carbon_emissions_2017",
//        sum("mangrove_carbon_emissions_2018") as "mangrove_carbon_emissions_2018"
      )
  }

  def nestYearData(df: DataFrame): DataFrame = {

    val spark: SparkSession = df.sparkSession
    import spark.implicits._

    df.select(
      $"iso",
      $"adm1",
      $"threshold",
      $"tcs",
      $"global_land_cover",
      $"primary_forest",
//      $"idn_primary_forest",
//      $"erosion",
//      $"biodiversity_significance",
//      $"biodiversity_intactness",
      $"wdpa",
      $"aze",
      $"plantations",
//      $"river_basin",
//      $"ecozone",
//      $"urban_watershed",
      $"mangroves_1996",
      $"mangroves_2016",
//      $"water_stress",
      $"ifl",
//      $"endemic_bird_area",
      $"tiger_cl",
      $"landmark",
      $"land_right",
      $"kba",
      $"mining",
//      $"rspo",
      $"idn_mys_peatlands",
      $"oil_palm",
      $"idn_forest_moratorium",
//      $"idn_land_cover",
//      $"mex_protected_areas",
//      $"mex_pes",
//      $"mex_forest_zoning",
//      $"per_production_forest",
//      $"per_protected_area",
//      $"per_forest_concession",
//      $"bra_biomes",
      $"wood_fiber",
      $"resource_right",
      $"managed_forests",
//      $"oil_gas",
      $"total_area",
      $"extent_2000",
      $"extent_2010",
      $"total_gain",
      $"total_biomass",
      $"weighted_biomass_per_ha",
      $"total_co2",
//      $"total_mangrove_biomass",
//      $"weighted_mangrove_biomass_per_ha",
//      $"total_mangrove_co2",
      array(
        struct(
          $"year_2001" as "year",
          $"area_loss_2001" as "area_loss",
          $"biomass_loss_2001" as "biomass_loss",
          $"carbon_emissions_2001" as "carbon_emissions"
          // $"mangrove_biomass_loss_2001" as "mangrove_biomass_loss",
          // $"mangrove_carbon_emissions_2001" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2002" as "year",
          $"area_loss_2002" as "area_loss",
          $"biomass_loss_2002" as "biomass_loss",
          $"carbon_emissions_2002" as "carbon_emissions"
//          $"mangrove_biomass_loss_2002" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2002" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2003" as "year",
          $"area_loss_2003" as "area_loss",
          $"biomass_loss_2003" as "biomass_loss",
          $"carbon_emissions_2003" as "carbon_emissions"
//          $"mangrove_biomass_loss_2003" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2003" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2004" as "year",
          $"area_loss_2004" as "area_loss",
          $"biomass_loss_2004" as "biomass_loss",
          $"carbon_emissions_2004" as "carbon_emissions"
//          $"mangrove_biomass_loss_2004" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2004" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2005" as "year",
          $"area_loss_2005" as "area_loss",
          $"biomass_loss_2005" as "biomass_loss",
          $"carbon_emissions_2005" as "carbon_emissions"
//          $"mangrove_biomass_loss_2005" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2005" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2006" as "year",
          $"area_loss_2006" as "area_loss",
          $"biomass_loss_2006" as "biomass_loss",
          $"carbon_emissions_2006" as "carbon_emissions"
//          $"mangrove_biomass_loss_2006" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2006" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2007" as "year",
          $"area_loss_2007" as "area_loss",
          $"biomass_loss_2007" as "biomass_loss",
          $"carbon_emissions_2007" as "carbon_emissions"
//          $"mangrove_biomass_loss_2007" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2007" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2008" as "year",
          $"area_loss_2008" as "area_loss",
          $"biomass_loss_2008" as "biomass_loss",
          $"carbon_emissions_2008" as "carbon_emissions"
//          $"mangrove_biomass_loss_2008" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2008" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2009" as "year",
          $"area_loss_2009" as "area_loss",
          $"biomass_loss_2009" as "biomass_loss",
          $"carbon_emissions_2009" as "carbon_emissions"
//          $"mangrove_biomass_loss_2009" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2009" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2010" as "year",
          $"area_loss_2010" as "area_loss",
          $"biomass_loss_2010" as "biomass_loss",
          $"carbon_emissions_2010" as "carbon_emissions"
//          $"mangrove_biomass_loss_2010" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2010" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2011" as "year",
          $"area_loss_2011" as "area_loss",
          $"biomass_loss_2011" as "biomass_loss",
          $"carbon_emissions_2011" as "carbon_emissions"
//          $"mangrove_biomass_loss_2011" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2011" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2012" as "year",
          $"area_loss_2012" as "area_loss",
          $"biomass_loss_2012" as "biomass_loss",
          $"carbon_emissions_2012" as "carbon_emissions"
//          $"mangrove_biomass_loss_2012" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2012" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2013" as "year",
          $"area_loss_2013" as "area_loss",
          $"biomass_loss_2013" as "biomass_loss",
          $"carbon_emissions_2013" as "carbon_emissions"
//          $"mangrove_biomass_loss_2013" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2013" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2014" as "year",
          $"area_loss_2014" as "area_loss",
          $"biomass_loss_2014" as "biomass_loss",
          $"carbon_emissions_2014" as "carbon_emissions"
//          $"mangrove_biomass_loss_2014" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2014" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2015" as "year",
          $"area_loss_2015" as "area_loss",
          $"biomass_loss_2015" as "biomass_loss",
          $"carbon_emissions_2015" as "carbon_emissions"
//          $"mangrove_biomass_loss_2015" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2015" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2016" as "year",
          $"area_loss_2016" as "area_loss",
          $"biomass_loss_2016" as "biomass_loss",
          $"carbon_emissions_2016" as "carbon_emissions"
//          $"mangrove_biomass_loss_2016" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2016" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2017" as "year",
          $"area_loss_2017" as "area_loss",
          $"biomass_loss_2017" as "biomass_loss",
          $"carbon_emissions_2017" as "carbon_emissions"
//          $"mangrove_biomass_loss_2017" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2017" as "mangrove_carbon_emissions"
        ),
        struct(
          $"year_2018" as "year",
          $"area_loss_2018" as "area_loss",
          $"biomass_loss_2018" as "biomass_loss",
          $"carbon_emissions_2018" as "carbon_emissions"
//          $"mangrove_biomass_loss_2018" as "mangrove_biomass_loss",
//          $"mangrove_carbon_emissions_2018" as "mangrove_carbon_emissions"
        )
      ) as "year_data"
    )
  }
}
