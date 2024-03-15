package no.uio.ifi.in2000.team22.badeapp.ui.screens.home

//Map import
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
import no.uio.ifi.in2000.team22.badeapp.R
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar


@OptIn(MapboxExperimental::class)
@Preview
@Composable
fun HomeScreen(homeScreenViewModel: HomeScreenViewModel = viewModel()) {
    val swimSpotUiState = homeScreenViewModel.swimSpotUiState.collectAsState()
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(10.0)
            center(Point.fromLngLat(10.7215, 59.9464))
            pitch(0.0)
            bearing(0.0)
        }
    }

    val marker =
        BitmapFactory.decodeResource(
            LocalContext.current.resources,
            R.drawable.ic_swimspot_location_on
        )

    Scaffold(
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar() }
    ) { paddingValues ->
        MapboxMap(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            mapViewportState = mapViewportState,
            mapInitOptionsFactory = { context ->
                MapInitOptions(
                    context = context,
                    styleUri = Style.OUTDOORS
                )
            },
            scaleBarSettings = ScaleBarSettings { enabled },
        ) {
            PointAnnotationGroup(
                annotations = swimSpotUiState.value.swimSpotList.map {
                    PointAnnotationOptions()
                        .withPoint(
                            Point.fromLngLat(it.lon, it.lat)
                        )
                        .withIconImage(marker)
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
            )
        }
    }
}