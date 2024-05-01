package no.uio.ifi.in2000.team22.badeapp.ui.components.loading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LoadingIndicator(onErrorText: String) {
    var showLoading by remember { mutableStateOf(true) }

    LaunchedEffect(showLoading) {
        delay(5000)
        showLoading = false
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (showLoading) {
            CircularProgressIndicator()
        } else {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "warning",
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Text(text = onErrorText)
        }
    }
}