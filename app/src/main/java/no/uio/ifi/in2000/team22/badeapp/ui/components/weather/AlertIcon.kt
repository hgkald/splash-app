package no.uio.ifi.in2000.team22.badeapp.ui.components.weather

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert

@Composable
fun AlertIcon(alert: Alert, modifier: Modifier) {
    val drawableName = "icon_warning_${alert.event}_${alert.riskMatrixColor}".lowercase()
    val drawableId : Int? = alertIconDrawable[drawableName]
    if (drawableId != null) {
        Image(
            modifier = modifier,
            painter = painterResource(
                id = drawableId
            ),
            contentDescription = drawableName
        )
    }
}

val alertIconDrawable = mapOf(
    "icon_warning_avalanches_orange" to R.drawable.icon_warning_avalanches_orange,
    "icon_warning_avalanches_red" to R.drawable.icon_warning_avalanches_red,
    "icon_warning_avalanches_yellow" to R.drawable.icon_warning_avalanches_yellow,
    "icon_warning_drivingconditions_orange" to R.drawable.icon_warning_drivingconditions_orange,
    "icon_warning_drivingconditions_red" to R.drawable.icon_warning_drivingconditions_red,
    "icon_warning_drivingconditions_yellow" to R.drawable.icon_warning_drivingconditions_yellow,
    "icon_warning_extreme" to R.drawable.icon_warning_extreme,
    "icon_warning_flood_orange" to R.drawable.icon_warning_flood_orange,
    "icon_warning_flood_red" to R.drawable.icon_warning_flood_red,
    "icon_warning_flood_yellow" to R.drawable.icon_warning_flood_yellow,
    "icon_warning_forestfire_orange" to R.drawable.icon_warning_forestfire_orange,
    "icon_warning_forestfire_red" to R.drawable.icon_warning_forestfire_red,
    "icon_warning_forestfire_yellow" to R.drawable.icon_warning_forestfire_yellow,
    "icon_warning_generic_orange" to R.drawable.icon_warning_generic_orange,
    "icon_warning_generic_red" to R.drawable.icon_warning_generic_red,
    "icon_warning_generic_yellow" to R.drawable.icon_warning_generic_yellow,
    "icon_warning_ice_orange" to R.drawable.icon_warning_ice_orange,
    "icon_warning_ice_red" to R.drawable.icon_warning_ice_red,
    "icon_warning_ice_yellow" to R.drawable.icon_warning_ice_yellow,
    "icon_warning_landslide_orange" to R.drawable.icon_warning_landslide_orange,
    "icon_warning_landslide_red" to R.drawable.icon_warning_landslide_red,
    "icon_warning_landslide_yellow" to R.drawable.icon_warning_landslide_yellow,
    "icon_warning_lightning_orange" to R.drawable.icon_warning_lightning_orange,
    "icon_warning_lightning_red" to R.drawable.icon_warning_lightning_red,
    "icon_warning_lightning_yellow" to R.drawable.icon_warning_lightning_yellow,
    "icon_warning_polarlow_orange" to R.drawable.icon_warning_polarlow_orange,
    "icon_warning_polarlow_red" to R.drawable.icon_warning_polarlow_red,
    "icon_warning_polarlow_yellow" to R.drawable.icon_warning_polarlow_yellow,
    "icon_warning_rain_orange" to R.drawable.icon_warning_rain_orange,
    "icon_warning_rain_red" to R.drawable.icon_warning_rain_red,
    "icon_warning_rain_yellow" to R.drawable.icon_warning_rain_yellow,
    "icon_warning_rainflood_orange" to R.drawable.icon_warning_rainflood_orange,
    "icon_warning_rainflood_red" to R.drawable.icon_warning_rainflood_red,
    "icon_warning_rainflood_yellow" to R.drawable.icon_warning_rainflood_yellow,
    "icon_warning_snow_orange" to R.drawable.icon_warning_snow_orange,
    "icon_warning_snow_red" to R.drawable.icon_warning_snow_red,
    "icon_warning_snow_yellow" to R.drawable.icon_warning_snow_yellow,
    "icon_warning_stormsurge_orange" to R.drawable.icon_warning_stormsurge_orange,
    "icon_warning_stormsurge_red" to R.drawable.icon_warning_stormsurge_red,
    "icon_warning_stormsurge_yellow" to R.drawable.icon_warning_stormsurge_yellow,
    "icon_warning_wind_orange" to R.drawable.icon_warning_wind_orange,
    "icon_warning_wind_red" to R.drawable.icon_warning_wind_red,
    "icon_warning_wind_yellow" to R.drawable.icon_warning_wind_yellow
)