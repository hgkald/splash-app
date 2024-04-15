package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.favorites.FavoritesListRepository
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot

data class FavUiState(
    val favList: List<Swimspot> = emptyList()
)

class FavoritesScreenViewModel : ViewModel(){
    private val _favUiState = MutableStateFlow(FavUiState())
    val favUiState : StateFlow<FavUiState> = _favUiState.asStateFlow()
    val favoritesListRepository = FavoritesListRepository()

    init {
        viewModelScope.launch {
            _favUiState.update {
                it.copy(favList = favoritesListRepository.getFavList())
            }
        }
        Log.d("FavoritesScreenViewModel", "Updating favoritesList. Updated list: " + favoritesListRepository.getFavList().toString())
    }

    fun toggleFavorite(swimspot: Swimspot){
        if (swimspot in favoritesListRepository.getFavList()){
            favoritesListRepository.removeFavorite(swimspot = swimspot)
        } else {
            favoritesListRepository.addFavorite(swimspot = swimspot)
        }
    }

    fun favorite(swimspot: Swimspot){
        favoritesListRepository.addFavorite(swimspot = swimspot)
    }

    fun unfavorite(swimspot: Swimspot){
        favoritesListRepository.removeFavorite(swimspot = swimspot)
    }

}