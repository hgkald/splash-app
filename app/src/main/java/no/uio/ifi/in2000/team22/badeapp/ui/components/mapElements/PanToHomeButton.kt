package no.uio.ifi.in2000.team22.badeapp.ui.components.mapElements

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team22.badeapp.R


@Composable
fun PanToHomeButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(30),
        contentPadding = PaddingValues(0.dp),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier
                .size(FloatingActionButtonDefaults.LargeIconSize)
                .padding(2.dp),
            imageVector = Icons.Default.Home,
            contentDescription = "Go to home location"
        )
    }
}
