package no.uio.ifi.in2000.team22.badeapp.ui.screens.home

//Map import
import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEvents
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.plugin.scalebar.generated.ScaleBarSettings
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

    Scaffold(
        topBar = { BadeAppTopAppBar() },
        bottomBar = { BadeAppBottomAppBar() }
    ) { paddingValues ->
        MapboxMap(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            mapViewportState = mapViewportState,
            scaleBarSettings = ScaleBarSettings { enabled },
            mapEvents = MapEvents(
                onCameraChanged = {
                    Log.i("MAP", it.cameraState.zoom.toString())
                    Log.i("MAP", it.cameraState.center.coordinates().toString())
                },
            ),
        ) {
            CircleAnnotation(
                point = Point.fromLngLat(10.7215, 59.9464),
                circleRadius = 10.0,
                circleOpacity = 10.0,
                circleColorInt = Color.RED
            )
        }
    }
}
