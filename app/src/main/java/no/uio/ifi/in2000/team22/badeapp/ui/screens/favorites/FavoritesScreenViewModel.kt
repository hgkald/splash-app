package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.SwimSpot

data class FavListUiState(
    val favList: List<SwimSpot> = emptyList()
)

class FavoritesScreenViewModel : ViewModel() {
    private val _favUiState = MutableStateFlow(FavListUiState())
    val favUiState : StateFlow<FavListUiState> = _favUiState.asStateFlow()

}