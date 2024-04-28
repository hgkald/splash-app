package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team22.badeapp.model.transport.TransportCategory

@Composable
fun TransportOverview(stops: List<TransportCategory>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        stops.map {
            val drawable = TransportCategory.toDrawable(it)
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(5.dp)
            ) {
                Icon(
                    painter = painterResource(id = drawable),
                    contentDescription = drawable.toString(),
                    tint = getColor(it),
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = TransportCategory.toNorwegian(it),
                    style = MaterialTheme.typography.labelSmall
                )
            }

        }
    }
}

private fun getColor(category: TransportCategory): Color {
    return when (category) {
        TransportCategory.BUS -> Color(
            red = 228,
            green = 47,
            blue = 43,
            alpha = 255
        )

        TransportCategory.BOAT -> Color(
            red = 77,
            green = 141,
            blue = 192,
            alpha = 255
        )

        TransportCategory.METRO -> Color(
            red = 255,
            green = 87,
            blue = 34,
            alpha = 255
        )

        TransportCategory.RAIL -> Color(
            red = 75,
            green = 173,
            blue = 79,
            alpha = 255
        )

        TransportCategory.TRAM -> Color(
            red = 77,
            green = 141,
            blue = 192,
            alpha = 255
        )
    }
}