package no.uio.ifi.in2000.team22.badeapp.ui.screens.home

//Map import
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEvents
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.WeatherDialog
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.WeatherFloatingActionButton


@OptIn(MapboxExperimental::class)
@Composable
fun HomeScreen(
    navcontroller: NavController,
    homeScreenViewModel: HomeScreenViewModel = viewModel()
) {
    var showWeatherDialog by remember { mutableStateOf(false) }

    val swimSpotUiState = homeScreenViewModel.swimSpotUiState.collectAsState()
    val weatherUiState = homeScreenViewModel.weatherUiState.collectAsState()
    val mapUiState = homeScreenViewModel.mapUiState.collectAsState()

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

        MapboxMap(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            mapViewportState = mapUiState.value.mapViewportState,
            scaleBarSettings = ScaleBarSettings {
                enabled;
                textSize = 25.0F
            }, //correct UU?
            mapEvents = MapEvents(
                onCameraChanged = {
                    Log.i("MAP", it.cameraState.zoom.toString())
                    Log.i("MAP", it.cameraState.center.coordinates().toString())
                    homeScreenViewModel.updatePosition(it.cameraState.center)
                },
            ),
            mapInitOptionsFactory = { context ->
                MapInitOptions(
                    context = context,
                    styleUri = Style.OUTDOORS
                )
            },
        ) {
            PointAnnotationGroup(
                annotations = mapUiState.value.mapPointsState.map { it.withIconImage(marker) },
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
            )
        }

        if (showWeatherDialog) {
            WeatherDialog(
                weatherUiState = weatherUiState,
                onDismissRequest = { showWeatherDialog = false }
            )
        }

    }
}