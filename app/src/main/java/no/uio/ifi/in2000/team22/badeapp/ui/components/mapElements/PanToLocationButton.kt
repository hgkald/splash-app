package no.uio.ifi.in2000.team22.badeapp.ui.components.mapElements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import no.uio.ifi.in2000.team22.badeapp.R


@Composable
fun PanToLocationButton(
    point: Point?,
    onClick: () -> Unit,
    modifier: Modifier
) {
    ElevatedButton(
        modifier = modifier,
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        val painter =
            if (point == null) {
                painterResource(id = R.drawable.location_searching_24px)
            }
            else {
                painterResource(id = R.drawable.my_location_24px)
            }
        Icon(
            modifier = Modifier
                .size(FloatingActionButtonDefaults.LargeIconSize)
                .padding(3.dp),
            painter = painter,
            contentDescription = "Go to user location"
        )
    }
}
