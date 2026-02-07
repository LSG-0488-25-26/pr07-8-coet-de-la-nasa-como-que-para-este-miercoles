package com.example.umafacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.umafacts.api.APIInterface
import com.example.umafacts.api.Repository
import com.example.umafacts.model.CharacterImageResponse
import com.example.umafacts.model.UmamusumeDetail
import kotlinx.coroutines.launch

data class UmaDetailState(
    val character: UmamusumeDetail? = null,
    val images: List<CharacterImageResponse> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class UmaDetailViewModel : ViewModel() {
    private val repository = Repository(APIInterface.create())

    private val _state = MutableLiveData<UmaDetailState>(UmaDetailState())
    val state: LiveData<UmaDetailState> = _state

    fun loadCharacterDetails(character: UmamusumeDetail?) {
        viewModelScope.launch {
            // Check if character is null
            if (character == null) {
                _state.value = UmaDetailState(
                    error = "Character not found",
                    isLoading = false
                )
                return@launch
            }

            _state.value = _state.value?.copy(
                isLoading = true,
                error = null,
                character = character
            ) ?: UmaDetailState(
                isLoading = true,
                character = character
            )

            repository.getCharacterImages(character.id)
                .onSuccess { images ->
                    _state.value = _state.value?.copy(
                        images = images,
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value?.copy(
                        error = error.message ?: "Failed to load images",
                        isLoading = false
                    )
                }
        }
    }
}