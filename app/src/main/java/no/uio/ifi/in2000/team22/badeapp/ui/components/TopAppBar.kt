package no.uio.ifi.in2000.team22.badeapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadeAppTopAppBar() {
    CenterAlignedTopAppBar(
        title = { Text("APPNAVN") },
        modifier = Modifier.fillMaxWidth()
    )
}