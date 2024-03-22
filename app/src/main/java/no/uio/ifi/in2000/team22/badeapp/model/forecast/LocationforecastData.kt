package no.uio.ifi.in2000.team22.badeapp.model.forecast

/*
This data class is for the Locationforecast API, for example:
  https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=60.10&lon=9.58
or via the UiO proxy:
  https://gw-uio.intark.uh-it.no/in2000/weatherapi/locationforecast/2.0/compact?lat=60.10&lon=9.58

Documentation of the data model can be viewed at:
  https://api.met.no/doc/locationforecast/datamodel
*/

data class Locationforecast(
    val type: String,
    val geometry: Geometry,
    val properties: Properties,
){
    data class Geometry(
        val type: String,
        val coordinates: List<Double>
    )

    data class Properties(
        val meta: Meta,
        val timeseries: List<TimeseriesEntry>
    ) {
        data class Meta(
            val updated_at: String, //TODO: timestamp type
            val units: Units
        ) {
            data class Units(
                val air_pressure_at_sea_level: String,
                val air_temperature: String,
                val cloud_area_fraction: String,
                val precipitation_amount: String,
                val relative_humidity: String,
                val wind_from_direction: String,
                val wind_speed: String
            )
        }

        data class TimeseriesEntry(
            val time: String, //TODO: timestamp type
            val data: Data?
        ) {
            data class Data(
                val instant: Instant?,
                val next_12_hours: Next12Hours?,
                val next_1_hours: Next1Hours?,
                val next_6_hours: Next6Hours?,
            ) {
                data class Instant(
                    val details: Details?
                ){
                    data class Details(
                        val air_pressure_at_sea_level: Double?,
                        val air_temperature: Double?,
                        //val air_temperature_percentile_10 : Double?,
                        //val air_temperature_percentile_90 : Double?,
                        val cloud_area_fraction : Double?,
                        //val cloud_area_fraction_high : Double?,
                        //val cloud_area_fraction_low : Double?,
                        //val cloud_area_fraction_medium : Double?,
                        //val dew_point_temperature : Double?,
                        //val fog_area_fraction : Double?,
                        val relative_humidity : Double?,
                        val ultraviolet_index_clear_sky : Double?, //Not available in compact
                        val wind_from_direction : Double?,
                        val wind_speed : Double?,
                        //val wind_speed_of_gust : Double?,
                        //val wind_speed_percentile_10 : Double?,
                        //val wind_speed_percentile_90 : Double?,
                    )
                }

                data class Next1Hours(
                    val summary: Summary?,
                    val details: Details?,
                ) {
                    data class Summary(
                        val symbol_code : String?
                    )
                    data class Details (
                        val precipitation_amount: Double?,
                    )
                }

                data class Next6Hours(
                    val summary: Summary?,
                    val details: Details?,
                ) {
                    data class Summary(
                        val symbol_code : String?
                    )
                    data class Details (
                        //val air_temperature_max: Double?,
                        //val air_temperature_min: Double?,
                        val precipitation_amount: Double?,
                        //val precipitation_amount_max: Double?,
                        //val precipitation_amount_min: Double?,
                        //val probability_of_precipitation: Double?
                    )
                }

                data class Next12Hours(
                    val summary: Summary?,
                    val details: Details?,
                ) {
                    data class Summary(
                        val symbol_code : String?
                    )
                    data class Details (
                        val probability_of_precipitation: Double?
                    )
                }
            }
        }
    }
}

