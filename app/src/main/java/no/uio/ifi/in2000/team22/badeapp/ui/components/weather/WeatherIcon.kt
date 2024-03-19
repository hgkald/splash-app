package no.uio.ifi.in2000.team22.badeapp.ui.components.weather

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.model.forecast.CurrentWeather


@Composable
fun WeatherIcon(weather: CurrentWeather, modifier: Modifier) {
    if (weather.symbolCode != null) {
        val drawableId : Int? = weatherIconDrawable[weather.symbolCode.toString()]
        if (drawableId != null) {
            Image(
                modifier = modifier,
                painter = painterResource(
                    id = drawableId
                ),
                contentDescription = weather.symbolCode.toString(),
            )
        }
    }
}

val weatherIconDrawable = mapOf(
    "clearsky_day" to R.drawable.clearsky_day,
    "clearsky_night" to R.drawable.clearsky_night,
    "clearsky_polartwilight" to R.drawable.clearsky_polartwilight,
    "cloudy" to R.drawable.cloudy,
    "fair_day" to R.drawable.fair_day,
    "fair_night" to R.drawable.fair_night,
    "fair_polartwilight" to R.drawable.fair_polartwilight,
    "fog" to R.drawable.fog,
    "heavyrain" to R.drawable.heavyrain,
    "heavyrainandthunder" to R.drawable.heavyrainandthunder,
    "heavyrainshowers_day" to R.drawable.heavyrainshowers_day,
    "heavyrainshowers_night" to R.drawable.heavyrainshowers_night,
    "heavyrainshowers_polartwilight" to R.drawable.heavyrainshowers_polartwilight,
    "heavyrainshowersandthunder_day" to R.drawable.heavyrainshowersandthunder_day,
    "heavyrainshowersandthunder_night" to R.drawable.heavyrainshowersandthunder_night,
    "heavyrainshowersandthunder_polartwilight" to R.drawable.heavyrainshowersandthunder_polartwilight,
    "heavysleet" to R.drawable.heavysleet,
    "heavysleetandthunder" to R.drawable.heavysleetandthunder,
    "heavysleetshowers_day" to R.drawable.heavysleetshowers_day,
    "heavysleetshowers_night" to R.drawable.heavysleetshowers_night,
    "heavysleetshowers_polartwilight" to R.drawable.heavysleetshowers_polartwilight,
    "heavysleetshowersandthunder_day" to R.drawable.heavysleetshowersandthunder_day,
    "heavysleetshowersandthunder_night" to R.drawable.heavysleetshowersandthunder_night,
    "heavysleetshowersandthunder_polartwilight" to R.drawable.heavysleetshowersandthunder_polartwilight,
    "heavysnow" to R.drawable.heavysnow,
    "heavysnowandthunder" to R.drawable.heavysnowandthunder,
    "heavysnowshowers_day" to R.drawable.heavysnowshowers_day,
    "heavysnowshowers_night" to R.drawable.heavysnowshowers_night,
    "heavysnowshowers_polartwilight" to R.drawable.heavysnowshowers_polartwilight,
    "heavysnowshowersandthunder_day" to R.drawable.heavysnowshowersandthunder_day,
    "heavysnowshowersandthunder_night" to R.drawable.heavysnowshowersandthunder_night,
    "heavysnowshowersandthunder_polartwilight" to R.drawable.heavysnowshowersandthunder_polartwilight,
    "lightrain" to R.drawable.lightrain,
    "lightrainandthunder" to R.drawable.lightrainandthunder,
    "lightrainshowers_day" to R.drawable.lightrainshowers_day,
    "lightrainshowers_night" to R.drawable.lightrainshowers_night,
    "lightrainshowers_polartwilight" to R.drawable.lightrainshowers_polartwilight,
    "lightrainshowersandthunder_day" to R.drawable.lightrainshowersandthunder_day,
    "lightrainshowersandthunder_night" to R.drawable.lightrainshowersandthunder_night,
    "lightrainshowersandthunder_polartwilight" to R.drawable.lightrainshowersandthunder_polartwilight,
    "lightsleet" to R.drawable.lightsleet,
    "lightsleetandthunder" to R.drawable.lightsleetandthunder,
    "lightsleetshowers_day" to R.drawable.lightsleetshowers_day,
    "lightsleetshowers_night" to R.drawable.lightsleetshowers_night,
    "lightsleetshowers_polartwilight" to R.drawable.lightsleetshowers_polartwilight,
    "lightsleetshowersandthunder_day" to R.drawable.lightssleetshowersandthunder_day,
    "lightsleetshowersandthunder_night" to R.drawable.lightssleetshowersandthunder_night,
    "lightsleetshowersandthunder_polartwilight" to R.drawable.lightssleetshowersandthunder_polartwilight,
    "lightsnow" to R.drawable.lightsnow,
    "lightsnowandthunder" to R.drawable.lightsnowandthunder,
    "lightsnowshowers_day" to R.drawable.lightsnowshowers_day,
    "lightsnowshowers_night" to R.drawable.lightsnowshowers_night,
    "lightsnowshowers_polartwilight" to R.drawable.lightsnowshowers_polartwilight,
    "lightsnowshowersandthunder_day" to R.drawable.lightssnowshowersandthunder_day,
    "lightsnowshowersandthunder_night" to R.drawable.lightssnowshowersandthunder_night,
    "lightsnowshowersandthunder_polartwilight" to R.drawable.lightssnowshowersandthunder_polartwilight,
    "lightssleetshowersandthunder_day" to R.drawable.lightssleetshowersandthunder_day,
    "lightssleetshowersandthunder_night" to R.drawable.lightssleetshowersandthunder_night,
    "lightssleetshowersandthunder_polartwilight" to R.drawable.lightssleetshowersandthunder_polartwilight,
    "lightssnowshowersandthunder_day" to R.drawable.lightssnowshowersandthunder_day,
    "lightssnowshowersandthunder_night" to R.drawable.lightssnowshowersandthunder_night,
    "lightssnowshowersandthunder_polartwilight" to R.drawable.lightssnowshowersandthunder_polartwilight,
    "partlycloudy_day" to R.drawable.partlycloudy_day,
    "partlycloudy_night" to R.drawable.partlycloudy_night,
    "partlycloudy_polartwilight" to R.drawable.partlycloudy_polartwilight,
    "rain" to R.drawable.rain,
    "rainandthunder" to R.drawable.rainandthunder,
    "rainshowers_day" to R.drawable.rainshowers_day,
    "rainshowers_night" to R.drawable.rainshowers_night,
    "rainshowers_polartwilight" to R.drawable.rainshowers_polartwilight,
    "rainshowersandthunder_day" to R.drawable.rainshowersandthunder_day,
    "rainshowersandthunder_night" to R.drawable.rainshowersandthunder_night,
    "rainshowersandthunder_polartwilight" to R.drawable.rainshowersandthunder_polartwilight,
    "sleet" to R.drawable.sleet,
    "sleetandthunder" to R.drawable.sleetandthunder,
    "sleetshowers_day" to R.drawable.sleetshowers_day,
    "sleetshowers_night" to R.drawable.sleetshowers_night,
    "sleetshowers_polartwilight" to R.drawable.sleetshowers_polartwilight,
    "sleetshowersandthunder_day" to R.drawable.sleetshowersandthunder_day,
    "sleetshowersandthunder_night" to R.drawable.sleetshowersandthunder_night,
    "sleetshowersandthunder_polartwilight" to R.drawable.sleetshowersandthunder_polartwilight,
    "snow" to R.drawable.snow,
    "snowandthunder" to R.drawable.snowandthunder,
    "snowshowers_day" to R.drawable.snowshowers_day,
    "snowshowers_night" to R.drawable.snowshowers_night,
    "snowshowers_polartwilight" to R.drawable.snowshowers_polartwilight,
    "snowshowersandthunder_day" to R.drawable.snowshowersandthunder_day,
    "snowshowersandthunder_night" to R.drawable.snowshowersandthunder_night,
    "snowshowersandthunder_polartwilight" to R.drawable.snowshowersandthunder_polartwilight
)

