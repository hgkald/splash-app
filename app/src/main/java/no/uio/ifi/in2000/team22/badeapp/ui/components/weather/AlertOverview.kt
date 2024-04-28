package no.uio.ifi.in2000.team22.badeapp.ui.components.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.model.alerts.Alert
import no.uio.ifi.in2000.team22.badeapp.model.alerts.RiskMatrixColor
import java.time.Instant
import java.time.ZoneId

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
            Instant.now(),
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
            Instant.now(),
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
            Instant.now(),
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

@Composable
fun AlertOverview(alerts: List<Alert>) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        if (alerts.size == 1) {
            AlertView(
                alert = alerts[0],
                expanded = expanded,
                onAlertClick = { expanded = !expanded })
        } else if (alerts.size > 1) {
            CombinedAlertView(
                title = "Varsler i området",
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
    onExpandClick: () -> Unit,
) {
    var alertExpanded by remember { mutableStateOf(-1) }

    Column {
        Card(
            onClick = onExpandClick,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AlertIcon(
                    alert = alerts[0],
                    modifier = Modifier.padding(10.dp)
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis
                    )

                    val subtext = if (!expanded) {
                        "Trykk for å vise"
                    } else {
                        "Trykk for å lukke"
                    }
                    Text(
                        text = subtext,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis
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
        }

        val height = if (expanded) 300 else 0
        Card(
            modifier = Modifier
                .heightIn(max = height.dp)
        ) {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()
            AnimatedVisibility(visible = expanded) {
                LazyColumn(state = listState) {
                    item { Divider() }
                    alerts
                        .sortedByDescending { it.riskMatrixColor }
                        .forEachIndexed { index, alert ->
                            item {
                                AlertView(
                                    alert = alert,
                                    expanded = alertExpanded == index,
                                    onAlertClick = {
                                        alertExpanded =
                                            if (alertExpanded == index) {
                                                -1
                                            } else {
                                                index
                                            }
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(index = index * 2)
                                        }
                                    })
                            }
                            if (index != alerts.lastIndex) {
                                item { Divider() }
                            }
                        }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertView(alert: Alert, expanded: Boolean, onAlertClick: () -> Unit) {
    Card(
        onClick = onAlertClick,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
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
                        fontWeight = FontWeight(600),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${alert.riskMatrixColor.norsk} nivå",
                        style = MaterialTheme.typography.bodyMedium,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            val mod = if (expanded) Modifier.height(IntrinsicSize.Max) else Modifier.height(0.dp)
            Card(
                onClick = onAlertClick,
                modifier = mod
            ) {
                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier.padding(10.dp, 0.dp)
                    ) {
                        val endTime = Instant
                            .parse(alert.eventEndingTime.toString())
                            .atZone(ZoneId.of("Europe/Oslo"))
                        Text(
                            style = MaterialTheme.typography.bodySmall,
                            text = "Gyldig til " +
                                    "${endTime.dayOfMonth}.${endTime.monthValue}.${endTime.year}, " +
                                    "kl.${endTime.hour}",
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = alert.description,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight(600),
                            text = "Anbefalinger",
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            style = MaterialTheme.typography.bodyMedium,
                            text = alert.instruction,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
