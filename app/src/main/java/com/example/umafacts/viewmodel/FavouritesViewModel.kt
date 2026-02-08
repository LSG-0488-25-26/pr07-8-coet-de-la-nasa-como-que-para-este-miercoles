package com.example.umafacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.example.umafacts.model.UmamusumeDetail
import com.example.umafacts.repository.FavouritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val repository: FavouritesRepository
) : ViewModel() {

    fun isFavourite(characterId: Int) = repository.isFavourite(characterId).asLiveData()

    // Updated to take the object and image URL
    fun toggleFavourite(detail: UmamusumeDetail, uniformImageUrl: String) {
        viewModelScope.launch {
            repository.toggleFavourite(detail, uniformImageUrl)
        }
    }

    fun getAllFavourites() = repository.getAllFavourites().asLiveData()
}