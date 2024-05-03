package no.uio.ifi.in2000.team22.badeapp.ui.permissions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LocationPermissionDialog(
    onConfirmClick: () -> Unit,
    onDismissClick: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                text = "Bruk GPS-posisjon?"
            )
        },
        text = {
            Text(
                modifier = Modifier.padding(6.dp),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.bodyMedium,
                text = "Dette funksjonalitetet bruker GPS-posisjonen. " +
                        "For at dette skal fungere, må du gi appen tilgang til å bruke GPS-data fra enheten din."
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = onConfirmClick
            ) { Text("Gi tilgang") }
        },
        dismissButton = {
            Button(
                onClick = onDismissClick
            ) { Text("Lukk") }
        }
    )
}