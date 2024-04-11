package no.uio.ifi.in2000.team22.badeapp.ui.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team22.badeapp.data.favorites.FavoritesListRepositoryOld
import no.uio.ifi.in2000.team22.badeapp.model.swimspots.Swimspot


data class FavUiStateOld(
    val favList: List<Swimspot> = emptyList()
)

class FavoritesViewModelOld() : ViewModel() {
    private val _favUiState = MutableStateFlow(FavUiState())
    val favUiState : StateFlow<FavUiState> = _favUiState.asStateFlow()
    val favoritesListRepository = FavoritesListRepositoryOld()

    init {
        viewModelScope.launch {
            _favUiState.update {
                Log.d("FavoritesViewModel", "Updating favorites ui state")
                it.copy(
                    favList = favoritesListRepository.getFavorites()
                )
            }
        }
    }

    fun toggleFavoriteButton(swimspot: Swimspot){
        if (swimspot in loadFavoritesOld()){
            favoritesListRepository.removeFavorite(swimspot)
            Log.d("ToggleFavoriteButton", "Trying to update favoritesListRepository, unfavorite $swimspot")
        } else {
            favoritesListRepository.addFavorite(swimspot)
            Log.d("ToggleFavoriteButton", "Trying to update favoritesListRepository, favorite $swimspot")
        }

        viewModelScope.launch {
            _favUiState.update {
                Log.d("FavViewModel", _favUiState.value.favList.toString())
                it.copy(
                    favList = favoritesListRepository.getFavorites()
                )
            }
            Log.d("FavoritesScreenViewModel", "Updated favorites ${favoritesListRepository.getFavorites()}")
        }
    }

    fun loadFavoritesOld() : List<Swimspot> {
        return favoritesListRepository.getFavorites()
    }

}


private fun loadFavoritesTestOld() : List<Swimspot> {
    return listOf(Swimspot(0, "Åkrasand", 59.250681, 5.194152), Swimspot(1, "Stavasand", 59.232681, 5.184657), Swimspot(2, "Fotvatnet", 59.298138, 5.286767), Swimspot(3, "Sandvesand",59.171176, 5.195650 ))
}