package com.example.umafacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.example.umafacts.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: FavouritesRepository
) : ViewModel() {

    fun isFavourite(characterId: Int) = repository.isFavourite(characterId).asLiveData()

    fun toggleFavourite(characterId: Int) {
        viewModelScope.launch {
            repository.toggleFavourite(characterId)
        }
    }

    fun addFavourite(characterId: Int) {
        viewModelScope.launch {
            repository.addFavourite(characterId)
        }
    }

    fun removeFavourite(characterId: Int) {
        viewModelScope.launch {
            repository.removeFavourite(characterId)
        }
    }

    fun getAllFavourites() = repository.getAllFavourites().asLiveData()
}