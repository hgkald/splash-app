package no.uio.ifi.in2000.team22.badeapp.ui.screens.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppBottomAppBar
import no.uio.ifi.in2000.team22.badeapp.ui.components.BadeAppTopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(navcontroller: NavController) {
    var input by remember { mutableStateOf("") }
    val keyboard = LocalSoftwareKeyboardController.current
    keyboard?.show()

    Scaffold(
        topBar = { Text(text = "") },
        bottomBar = { BadeAppBottomAppBar(navcontroller) },
        )
    {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxSize()
    ) {

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 13.dp, end = 13.dp),
            value = input,
            shape = CircleShape,
            onValueChange = { input = it },
            label = { Text("Søk her:") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboard?.hide()
                    //buttonClicked = true
                }
            )
        )
        Spacer(modifier = Modifier.height(18.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 25.dp)
                .wrapContentSize(Alignment.TopEnd)
        ) {
            Dropdown()
        }
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown() {
    var expanded by remember {
        mutableStateOf(false)
    }

    var valgt by remember {
        mutableStateOf("")
    }
    Box(modifier = Modifier.fillMaxSize()) {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded = it}) {
            OutlinedTextField(
                value = valgt,
                readOnly = true,
                label = { Text(text = "Sorter etter") },
                onValueChange = {},
                trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}) {
            DropdownMenuItem(
                text = { Text(text = "alfabetisk") },
                onClick = {
                    expanded = false
                    valgt = "alfabetisk"}
            )
            DropdownMenuItem(
                text = { Text(text = "nærmest") },
                onClick = {
                    expanded = false
                    valgt = "nærmest"}
            )
        }
    }
    }
}

//Søker for hver bokstav