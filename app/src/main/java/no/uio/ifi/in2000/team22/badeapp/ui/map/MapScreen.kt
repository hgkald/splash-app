package no.uio.ifi.in2000.team22.badeapp.ui.map

import android.graphics.Color
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEvents
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation

@OptIn(MapboxExperimental::class)
@Preview
@Composable
fun MapScreen(mapScreenViewModel: MapScreenViewModel = viewModel()){
    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(10.0)
            center(Point.fromLngLat(10.7215, 59.9464))
            pitch(0.0)
            bearing(0.0)
        }
    }

    MapboxMap(
        modifier = Modifier.fillMaxSize(),
        mapViewportState = mapViewportState,
        mapEvents = MapEvents(
            onCameraChanged = {
                Log.i("MAP", it.cameraState.zoom.toString())
                Log.i("MAP", it.cameraState.center.coordinates().toString())
            },
        ),
    ){
        CircleAnnotation(
            point = Point.fromLngLat(10.7215, 59.9464),
            circleRadius = 10.0,
            circleOpacity = 10.0,
            circleColorInt = Color.RED
        )
    }
}