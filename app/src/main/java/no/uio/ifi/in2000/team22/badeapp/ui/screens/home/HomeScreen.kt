package no.uio.ifi.in2000.team22.badeapp.ui.screens.home

//Map import
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location.distanceBetween
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mapbox.api.geocoding.v5.GeocodingCriteria
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.api.geocoding.v5.models.GeocodingResponse
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapEvents
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.Screen
import no.uio.ifi.in2000.team22.badeapp.ui.components.mapElements.PanToHomeButton
import no.uio.ifi.in2000.team22.badeapp.ui.components.mapElements.PanToLocationButton
import no.uio.ifi.in2000.team22.badeapp.ui.components.mapElements.WeatherInfoButton
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.WeatherDialog
import no.uio.ifi.in2000.team22.badeapp.ui.permissions.LocationPermissionDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private data class SwimspotId(val id: String)

@OptIn(MapboxExperimental::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navcontroller: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    var showWeatherDialog by remember { mutableStateOf(false) }
    var showWeatherInfoButton by remember { mutableStateOf(true) }
    var weatherLocationName by remember { mutableStateOf<String?>(null) }

    var showLocationPermissionDialog by remember { mutableStateOf(false) }

    val swimSpotUiState = homeScreenViewModel.swimSpotUiState.collectAsState()
    val weatherUiState = homeScreenViewModel.weatherUiState.collectAsState()
    val mapUiState = homeScreenViewModel.mapUiState.collectAsState()
    val locationUiState = homeScreenViewModel.locationUiState.collectAsState()

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = { results: Map<String, Boolean> ->
            if (results[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                homeScreenViewModel.updateLocationPermissions()
                Log.d("test", locationUiState.value.locationPermissions.toString())
            } else if (results[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                homeScreenViewModel.updateLocationPermissions()
            }
        }
    )

    val marker =
        BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.drawable.ic_swimspot_location_on
        )

    val mapViewportState = rememberMapViewportState {
        // Set the initial camera position
        setCameraOptions {
            zoom(10.0)
            center(mapUiState.value.homeLocation)
            pitch(0.0)
            bearing(0.0)
        }
    }


    Scaffold(
        bottomBar = { BadeAppBottomAppBar(navcontroller, Screen.Home) },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                val lastKnownLocation = locationUiState.value.lastKnownLocation
                val userGpsLocation = if (lastKnownLocation != null) {
                    Point.fromLngLat(
                        lastKnownLocation.longitude,
                        lastKnownLocation.latitude
                    )
                } else {
                    null
                }

                PanToLocationButton(
                    point = userGpsLocation,
                    onClick = {
                        if (locationUiState.value.locationPermissions) {
                            mapViewportState.easeTo(
                                cameraOptions = cameraOptions {
                                    center(userGpsLocation)
                                    pitch(0.0)
                                },
                                MapAnimationOptions.mapAnimationOptions { duration(1000) }
                            )
                        } else {
                            showLocationPermissionDialog = true
                        }
                    },
                    modifier = Modifier
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                PanToHomeButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .height(60.dp)
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MapboxMap(
                mapViewportState = mapViewportState,
                scaleBarSettings = ScaleBarSettings {
                    enabled
                    textSize = 25.0F
                }, //correct UU?
                mapEvents = MapEvents(
                    onCameraChanged = {
                        /*Log.i("MAP", it.cameraState.zoom.toString())
                        Log.i("MAP", it.cameraState.center.coordinates().toString())*/
                        if (it.cameraState.zoom >= 9) {
                            showWeatherInfoButton = true
                            val results: FloatArray = floatArrayOf(0F)
                            distanceBetween(
                                weatherUiState.value.weatherLocation.latitude(),
                                weatherUiState.value.weatherLocation.longitude(),
                                it.cameraState.center.latitude(),
                                it.cameraState.center.longitude(),
                                results
                            )
                            if (results[0] > 10000) {
                                /* Log.i("HomeScreen", "Getting new weather (>20km)")*/
                                homeScreenViewModel.updateWeatherLocation(
                                    Point.fromLngLat(
                                        it.cameraState.center.longitude(),
                                        it.cameraState.center.latitude()
                                    )
                                )
                                homeScreenViewModel.updateWeather()
                            }
                        } else {
                            showWeatherInfoButton = false
                        }
                    },
                ),
                mapInitOptionsFactory = { context ->
                    MapInitOptions(
                        context = context,
                        styleUri = Style.OUTDOORS
                    )
                },
                locationComponentSettings = LocationComponentSettings(
                    createDefault2DPuck(),
                ) {
                    enabled = true
                }
            ) {
                PointAnnotationGroup(
                    annotations = swimSpotUiState.value.swimspotList.map { swimspot ->
                        PointAnnotationOptions()
                            .withPoint(Point.fromLngLat(swimspot.lon, swimspot.lat))
                            .withIconImage(marker)
                            .withData(
                                Gson().fromJson(
                                    "{id: ${swimspot.id}}",
                                    JsonElement::class.java
                                )
                            )
                    },
                    annotationConfig = AnnotationConfig(
                        annotationSourceOptions = AnnotationSourceOptions(
                            clusterOptions = ClusterOptions(
                                textColor = Color.WHITE, // Will not be applied as textColorExpression has been set
                                textSize = 20.0,
                                circleRadiusExpression = literal(25.0),
                                colorLevels = listOf(
                                    Pair(0, Color.parseColor("#3947A3"))
                                ),
                                clusterMaxZoom = 10
                            )
                        )
                    ),
                    onClick = {
                        val id = Gson().fromJson(it.getData(), SwimspotId::class.java).id
                        Log.d("HomeScreen", "Navigating to swimspot: $id")
                        navcontroller.run { navigate("swimspot/${id}") }
                        true
                    }
                )
            }

            if (showWeatherInfoButton) {
                WeatherInfoButton(
                    weather = weatherUiState.value.weather,
                    alerts = weatherUiState.value.metAlerts,
                    onClick = {
                        showWeatherDialog = true
                    },
                    modifier = Modifier
                        .padding(12.dp)
                        .height(50.dp)
                        .align(Alignment.TopEnd)
                )
            }
        }

        @OptIn(MapboxExperimental::class)
        fun getPlaceNameFromCoordinates(
            weatherUiState: State<WeatherUiState>,
            context: Context,
            mapViewportState: MapViewportState,
        ) {
            val reverseGeocode = MapboxGeocoding.builder()
                .accessToken(ContextCompat.getString(context, R.string.mapbox_key))
                .query(weatherUiState.value.weatherLocation)
                .geocodingTypes(GeocodingCriteria.TYPE_PLACE)
                .languages("no")
                .build()

            reverseGeocode.enqueueCall(object : Callback<GeocodingResponse> {
                override fun onResponse(
                    call: Call<GeocodingResponse>,
                    response: Response<GeocodingResponse>
                ) {

                    val results = response.body()!!.features()

                    if (results.size > 0) {
                        if (mapViewportState.cameraState.zoom >= 10) {
                            weatherLocationName = results[0].text()
                            Log.d("HomeScreen", "onResponse: ${results[0].text()}")
                        } else if (mapViewportState.cameraState.zoom >= 8) {
                            weatherLocationName =
                                results[0].text() //getKommuneName(results[0].placeName())
                            Log.d("HomeScreen", "onResponse: ${results[0].placeName()}")
                        } else {
                            weatherLocationName = null
                        }
                    } else {
                        weatherLocationName = null
                        Log.d("HomeScreen", "onResponse: No result found")

                    }
                }

                override fun onFailure(call: Call<GeocodingResponse>, throwable: Throwable) {
                    throwable.printStackTrace()
                }

            })
        }
        if (showWeatherDialog) {
            getPlaceNameFromCoordinates(weatherUiState, LocalContext.current, mapViewportState)

            WeatherDialog(
                weather = weatherUiState.value.weather,
                metAlerts = weatherUiState.value.metAlerts,
                locationName = weatherLocationName,
                onDismissRequest = { showWeatherDialog = false },
            )
        }

        if (locationPermissionsState.permissions[0].status.isGranted) {
            homeScreenViewModel.updateLocationPermissions()
        }
        if (showLocationPermissionDialog) {
            Log.d("HomeScreen", locationPermissionsState.permissions[1].status.toString())
            LocationPermissionDialog(
                onConfirmClick = {
                    locationPermissionsState.launchMultiplePermissionRequest()
                    showLocationPermissionDialog = false
                },
                onDismissClick = { showLocationPermissionDialog = false },
                onDismissRequest = { showLocationPermissionDialog = false }
            )
        }
    }


}
