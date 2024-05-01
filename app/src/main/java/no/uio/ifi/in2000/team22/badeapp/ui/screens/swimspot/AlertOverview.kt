package no.uio.ifi.in2000.team22.badeapp.ui.screens.swimspot

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.alerts.RiskMatrixColor
import no.uio.ifi.in2000.team22.badeapp.ui.components.weather.AlertIcon

@Preview(showSystemUi = true)
@Composable
fun AlertOverviewPreview() {
    val alerts = listOf<Alert>(
        Alert(
            geographicArea = emptyList(),
            "",
            "",
            "",
            "",
            "",
            "",
            "flood",
            "Test",
            "",
            "",
            "",
            emptyList(),
            RiskMatrixColor.Red,
            ""
        ),
        Alert(
            geographicArea = emptyList(),
            "",
            "",
            "",
            "",
            "",
            "",
            "flood",
            "Test",
            "",
            "",
            "",
            emptyList(),
            RiskMatrixColor.Yellow,
            ""
        ),
        Alert(
            geographicArea = emptyList(),
            "",
            "",
            "",
            "",
            "",
            "",
            "flood",
            "Test",
            "",
            "",
            "",
            emptyList(),
            RiskMatrixColor.Orange,
            ""
        )
    )
    AlertOverview(alerts = alerts)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertOverview(alerts: List<Alert>) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        if (alerts.size == 1) {
            AlertView(alert = alerts[0])
        } else if (alerts.size > 1) {
            CombinedAlertView(
                title = "Flere farevarsler i dette området",
                alerts = alerts,
                expanded = expanded,
                onExpandClick = {
                    expanded = !expanded
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinedAlertView(
    title: String,
    alerts: List<Alert>,
    expanded: Boolean,
    onExpandClick: () -> Unit
) {
    Card(
        onClick = onExpandClick,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AlertIcon(
                alert = alerts[0], modifier = Modifier
                    .padding(10.dp)
            )
            Column(
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )

                val subtext = if (!expanded) {
                    "Trykk her for å utvide oversikten"
                } else {
                    "Trykk her for å lukke oversikten"
                }
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            val stateIcon = if (expanded) {
                Icons.Default.KeyboardArrowUp
            } else {
                Icons.Default.KeyboardArrowDown
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = stateIcon,
                contentDescription = "open alert overview",
                modifier = Modifier.padding(20.dp)
            )
        }
        AnimatedVisibility(visible = expanded) {
            Column {
                Divider()
                alerts
                    .sortedByDescending { it.riskMatrixColor }
                    .forEachIndexed { index, alert ->
                        AlertView(alert = alert)
                        if (index != alerts.lastIndex) {
                            Divider()
                        }
                    }
            }
        }
    }
}

@Composable
fun AlertView(alert: Alert) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AlertIcon(alert = alert, modifier = Modifier.padding(12.dp))
            Column {
                Text(
                    text = alert.eventAwarenessName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight(600)
                )
                Text(
                    text = "${alert.riskMatrixColor.norsk} nivå",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}