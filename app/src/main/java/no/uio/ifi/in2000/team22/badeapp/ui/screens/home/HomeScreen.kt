package no.uio.ifi.in2000.team22.badeapp.ui.screens.home

//Map import
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.compose.MapboxMap
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
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.WeatherDialog
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.WeatherFloatingActionButton
import no.uio.ifi.in2000.team22.badeapp.ui.permissions.LocationPermissionDialog

private data class SwimspotId(val id: String)

@OptIn(MapboxExperimental::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navcontroller: NavController,
    homeScreenViewModel: HomeScreenViewModel
) {
    var showWeatherDialog by remember { mutableStateOf(false) }
    var lastKnownLocation by remember { mutableStateOf(Point.fromLngLat(10.0, 59.0)) }

    var showLocationPermissionDialog by remember { mutableStateOf(false) }

    val swimSpotUiState = homeScreenViewModel.swimSpotUiState.collectAsState()
    val weatherUiState = homeScreenViewModel.weatherUiState.collectAsState()
    val mapUiState = homeScreenViewModel.mapUiState.collectAsState()
    //val lastKnownLocation = homeScreenViewModel.lastKnownLocation.collectAsState()

    //TODO: Should be moved to the viewmodel
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(LocalContext.current)

    //TODO: Should be moved to the viewmodel
    fun saveLastKnownLocation() {
        try {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        lastKnownLocation = Point.fromLngLat(location.longitude, location.latitude)
                        homeScreenViewModel.isGpsLocationKnown(true)
                        Log.i(
                            "HomeScreen",
                            "savelastKnownLocation(): ${lastKnownLocation.longitude()} ${lastKnownLocation.latitude()}"
                        )
                    } else {
                        Log.i("HomeScreen", "savelastKnownLocation(): Could not get location")
                        homeScreenViewModel.isGpsLocationKnown(false)
                    }
                }
        } catch (e: SecurityException) {
            Log.w("HomeScreen", "Cannot get last location: Inadequate permissions")
            Log.w("HomeScreen", e.message.toString())
            Log.w("HomeScreen", e.stackTrace.toString())
        }
    }

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        ),
        onPermissionsResult = { results: Map<String, Boolean> ->
            if (results[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                saveLastKnownLocation()
            } else if (results[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                saveLastKnownLocation()
            }
        }
    )

    val marker =
        BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.drawable.ic_swimspot_location_on
        )

    Scaffold(
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar(navcontroller) },
        floatingActionButton = {
            WeatherFloatingActionButton(
                weather = weatherUiState.value.weather,
                onClick = {
                    showWeatherDialog = true
                },
            )
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val mapViewportState = rememberMapViewportState {
                // Set the initial camera position
                setCameraOptions {
                    zoom(10.0)
                    center(mapUiState.value.mapPositionState)
                    pitch(0.0)
                    bearing(0.0)
                }
            }
            MapboxMap(
                mapViewportState = mapViewportState,
                scaleBarSettings = ScaleBarSettings {
                    enabled;
                    textSize = 25.0F
                }, //correct UU?
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

            val locationButtonOnClick =
                if (mapUiState.value.gpsLocationKnown) {
                    {
                        mapViewportState.easeTo(
                            cameraOptions = cameraOptions {
                                center(lastKnownLocation)
                                zoom(10.0)
                                pitch(0.0)
                            },
                            MapAnimationOptions.mapAnimationOptions { duration(1000) }
                        )
                    }
                } else {
                    { showLocationPermissionDialog = true }
                }

            Button(
                modifier = Modifier
                    .padding(24.dp)
                    .height(60.dp)
                    .aspectRatio(1.0F),
                shape = ButtonDefaults.elevatedShape,
                colors = ButtonDefaults.elevatedButtonColors(),
                elevation = ButtonDefaults.elevatedButtonElevation(),
                contentPadding = PaddingValues(0.dp),
                onClick = locationButtonOnClick
            ) {
                Icon(
                    modifier = Modifier
                        .size(FloatingActionButtonDefaults.LargeIconSize)
                        .padding(4.dp),
                    imageVector = Icons.Default.Place,
                    contentDescription = "Go to user location"
                )
            }
        }

        if (showWeatherDialog) {
            WeatherDialog(
                weatherUiState = weatherUiState,
                onDismissRequest = { showWeatherDialog = false }
            )
        }

        if (showLocationPermissionDialog) {
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