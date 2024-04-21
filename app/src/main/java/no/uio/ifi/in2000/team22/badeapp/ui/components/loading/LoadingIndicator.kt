package no.uio.ifi.in2000.team22.badeapp.ui.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
@Preview(showSystemUi = true)
fun LoadingIndicatorPreview() {
    LoadingIndicator(onErrorText = "Ingen registrert vannemperatur for dette badestedet")
}

@Composable
fun LoadingIndicator(onErrorText: String) {
    var showLoading by remember { mutableStateOf(false) }

    LaunchedEffect(showLoading) {
        delay(5000)
        showLoading = false
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (showLoading) {
            CircularProgressIndicator()
        } else {
            Icon(
                imageVector = Icons.Rounded.Warning,
                contentDescription = "warning",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Text(
                text = onErrorText,
                textAlign = TextAlign.Center,
            )
        }
    }
}